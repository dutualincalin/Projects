package com.tema2sprc.service;

import com.tema2sprc.entity.TemperatureEntity;
import com.tema2sprc.exceptions.BadRequestException;
import com.tema2sprc.exceptions.DataConflictException;
import com.tema2sprc.exceptions.NotFoundException;
import com.tema2sprc.mapper.ObjectMapper;
import com.tema2sprc.pojo.City;
import com.tema2sprc.pojo.Temperature;
import com.tema2sprc.pojo.TemperatureOutput;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemperatureService {
    private List<Temperature> temperatureList;

    private final DatabaseService databaseService;

    private final CityService cityService;

    private final CountryService countryService;

    private final DataConflictException dataConflictException;

    private final NotFoundException notFoundException;

    private final BadRequestException badRequestException;

    private int entityId;

    public TemperatureService(DatabaseService databaseService,
                              CityService cityService, CountryService countryService,
                              DataConflictException dataConflictException,
                              NotFoundException notFoundException,
                              BadRequestException badRequestException) {
        this.databaseService = databaseService;
        this.cityService = cityService;
        this.countryService = countryService;
        this.dataConflictException = dataConflictException;
        this.notFoundException = notFoundException;
        temperatureList = databaseService.getTemperatureData();
        this.badRequestException = badRequestException;
        entityId = 1;
    }

    @Transactional
    public int addNewTemperature (Temperature temperature) {
        if(temperature.getIdOras() == 0 || temperature.getValoare() == null){
            throw badRequestException;
        }

        if(cityService.getCityById(temperature.getIdOras()).isEmpty()){
            throw notFoundException;
        }

        Date timestamp = new Date();

        temperature.setId(entityId);
        entityId++;
        temperature.setTimestamp(timestamp);

        databaseService.addTo(temperature);
        temperatureList.add(temperature);
        return temperature.getId();
    }

    public List<TemperatureOutput> getTemperaturesByCoordinatesAndDate (Double lat, Double lon, Date from,
                                                                  Date until) {
        List<City> cityList = cityService.getCityList().stream().filter(city ->
                (lat == null || city.getLat().equals(lat))
                && (lon == null || city.getLon().equals(lon))
        ).collect(Collectors.toList());

        return temperatureList.stream().filter(
            temperature -> (from == null || temperature.getTimestamp().after(from))
                && (until == null || temperature.getTimestamp().before(until))
                && ((lat == null && lon == null) ||
                        cityService.getCityById(temperature.getIdOras(), cityList).isPresent()
        )).map(ObjectMapper.instance::InstanceToOutput).collect(Collectors.toList());
    }

    public List<TemperatureOutput> getTemperaturesByCityAndDate (int id, Date from, Date until) {
        City city = cityService.getCityById(id).orElseThrow(() -> notFoundException);

        return temperatureList.stream().filter(temperature ->
            temperature.getIdOras() == city.getId()
            && (from == null || temperature.getTimestamp().after(from))
            && (until == null || temperature.getTimestamp().before(until))
        ).map(ObjectMapper.instance::InstanceToOutput).collect(Collectors.toList());
    }

    public List<TemperatureOutput> getTemperaturesByCountryAndDate (int id, Date from, Date until) {
        if(countryService.findCountryById(id).isEmpty()) {
            throw notFoundException;
        }

        List<City> cityList = cityService.getCitiesByCountryId(id);

        if (cityList.isEmpty()){
            throw notFoundException;
        }

        return temperatureList.stream().filter(temperature ->
                cityService.getCityById(temperature.getIdOras(), cityList).isPresent()
                        && (from == null || temperature.getTimestamp().after(from))
                        && (until == null || temperature.getTimestamp().before(until))
        ).map(ObjectMapper.instance::InstanceToOutput).collect(Collectors.toList());
    }

    public void modifyTemperature(int id, Temperature temperature) {
        if(id <= 0 || temperature.getId() != id){
            throw badRequestException;
        }

        if(getTemperatureById(id).isEmpty()){
            throw notFoundException;
        }

        TemperatureEntity temperatureEntity = databaseService.getTemperatureReference(id);

        if(temperature.getIdOras() != 0 && temperatureEntity.getIdOras() != temperature.getIdOras()) {
            if(getTemperatureByCityAndTimestamp(
                    temperature.getIdOras(),
                    temperatureEntity.getTimestamp())
                    .isPresent())
            {
                throw dataConflictException;
            }
            temperatureEntity.setIdOras(temperature.getIdOras());
        }

        if(temperature.getValoare() != null && !temperatureEntity.getValoare().equals(temperature.getValoare())) {
            temperatureEntity.setValoare(temperature.getValoare());
        }

        temperatureList = databaseService.saveAndUpdate(temperatureEntity);
    }

    public void deleteTemperature(int id) {
        if(getTemperatureById(id).isEmpty()){
            throw notFoundException;
        }

        temperatureList = databaseService.deleteTemperatureAndUpdate(id);
    }

    public Optional<Temperature> getTemperatureById(int id) {
        return temperatureList.stream()
            .filter(temperature -> temperature.getId() == id)
            .findFirst();
    }

    public Optional<Temperature> getTemperatureByCityAndTimestamp(int id, Date timestamp) {
        return temperatureList.stream()
                .filter(
                    temperature -> temperature.getIdOras() == id
                    && temperature.getTimestamp().equals(timestamp)
                ).findFirst();
    }
}
