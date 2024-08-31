package com.weatherforecast.service;

import com.weatherforecast.entity.CityEntity;
import com.weatherforecast.entity.CountryEntity;
import com.weatherforecast.entity.TemperatureEntity;
import com.weatherforecast.exceptions.DataConflictException;
import com.weatherforecast.mapper.ObjectMapper;
import com.weatherforecast.pojo.City;
import com.weatherforecast.pojo.Country;
import com.weatherforecast.pojo.Temperature;
import com.weatherforecast.repository.CityRepository;
import com.weatherforecast.repository.CountryRepository;
import com.weatherforecast.repository.TemperatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseService {
    private final CityRepository cityRepository;

    private final CountryRepository countryRepository;

    private final TemperatureRepository temperatureRepository;

    private final DataConflictException dataConflictException;

    public void addTo(City city){
        CityEntity cityEntity = ObjectMapper.instance.InstanceToEntity(city);
        cityRepository.save(cityEntity);
    }

    public void addTo(Country country){
        CountryEntity countryEntity = ObjectMapper.instance.InstanceToEntity(country);
        countryRepository.save(countryEntity);
    }

    public void addTo(Temperature temperature){
        TemperatureEntity temperatureEntity = ObjectMapper.instance.InstanceToEntity(temperature);
        temperatureRepository.save(temperatureEntity);
    }

    public List<City> getCityData() {
        List<CityEntity> cityEntities = cityRepository.findAll();

        if (cityEntities.isEmpty()){
            return new ArrayList<>();
        }

        return cityEntities.stream().map(ObjectMapper.instance::EntityToInstance)
                .collect(Collectors.toList());
    }

    public List<Country> getCountryData() {
        List<CountryEntity> countryEntities = countryRepository.findAll();

        if(countryEntities.isEmpty()){
            return new ArrayList<>();
        }

        return countryEntities.stream().map(ObjectMapper.instance::EntityToInstance)
                .collect(Collectors.toList());
    }

    public List<Temperature> getTemperatureData() {
        List<TemperatureEntity> temperatureEntities = temperatureRepository.findAll();

        if(temperatureEntities.isEmpty()){
            return new ArrayList<>();
        }

        return temperatureEntities.stream().map(ObjectMapper.instance::EntityToInstance)
                .collect(Collectors.toList());
    }

    public boolean checkIfCountryExists(int id) {
        return countryRepository.existsById(id);
    }

    public boolean checkIfCityExists(int id) {
        return cityRepository.existsById(id);
    }

    public boolean checkIfTemperatureExists(int id) {
        return temperatureRepository.existsById(id);
    }

    public CountryEntity getCountryReference(int id) {
        return countryRepository.getReferenceById(id);
    }

    public CityEntity getCityReference(int id) {
        return cityRepository.getReferenceById(id);
    }

    public TemperatureEntity getTemperatureReference(int id) {
        return temperatureRepository.getReferenceById(id);
    }

    public List<Country> saveAndUpdate(CountryEntity countryEntity) {
        countryRepository.save(countryEntity);
        return getCountryData();
    }

    public List<City> saveAndUpdate(CityEntity cityEntity) {
        cityRepository.save(cityEntity);
        return getCityData();
    }

    public List<Temperature> saveAndUpdate(TemperatureEntity temperatureEntity) {
        temperatureRepository.save(temperatureEntity);
        return getTemperatureData();
    }

    public List<Country> deleteCountryAndUpdate(int id) {
        countryRepository.deleteById(id);
        return getCountryData();
    }

    public List<City> deleteCityAndUpdate(int id) {
        cityRepository.deleteById(id);
        return getCityData();
    }

    public List<Temperature> deleteTemperatureAndUpdate(int id) {
        temperatureRepository.deleteById(id);
        return getTemperatureData();
    }
}
