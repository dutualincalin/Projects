package com.tema2sprc.service;

import com.tema2sprc.entity.CityEntity;
import com.tema2sprc.exceptions.BadRequestException;
import com.tema2sprc.exceptions.DataConflictException;
import com.tema2sprc.exceptions.NotFoundException;
import com.tema2sprc.pojo.City;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {
    private int entityId;

    private List<City> cityList;

    private final DatabaseService databaseService;

    private final DataConflictException dataConflictException;

    private final NotFoundException notFoundException;

    private final BadRequestException badRequestException;

    private final CountryService countryService;

    public CityService(DatabaseService databaseService, DataConflictException dataConflictException,
                       NotFoundException notFoundException, BadRequestException badRequestException,
                       CountryService countryService) {
        this.databaseService = databaseService;
        this.dataConflictException = dataConflictException;
        this.notFoundException = notFoundException;
        this.cityList = databaseService.getCityData();
        this.badRequestException = badRequestException;
        this.countryService = countryService;
        entityId = 1;
    }

    @Transactional
    public int addNewCity(City city) {
        if(
            city.getNume() == null || city.getIdTara() == 0
            || city.getLat() == null || city.getLon() == null){
            throw badRequestException;
        }

        if (countryService.findCountryById(city.getIdTara()).isEmpty()) {
            throw notFoundException;
        }

        if(findCityByNameAndCountry(city.getIdTara(), city.getNume()).isPresent()){
            throw dataConflictException;
        }

        city.setId(entityId);
        entityId++;
        cityList.add(city);
        databaseService.addTo(city);

        return city.getId();
    }

    public List<City> getCityList(){
        return cityList;
    }

    public List<City> getCitiesByCountryId(int id_tara) {
        return cityList.stream()
                .filter(city -> city.getIdTara() == id_tara)
                .collect(Collectors.toList());
    }

    public Optional<City> getCityById(int id) {
        return cityList.stream()
                .filter(city -> city.getId() == id)
                .findFirst();
    }

    public Optional<City> getCityById(int id, List<City> cityList) {
        return cityList.stream()
                .filter(city -> city.getId() == id)
                .findFirst();
    }

    public void modifyCity (int id, City city) {
        if(id <= 0 || city.getId() != id){
            throw badRequestException;
        }

        if(getCityById(id).isEmpty()){
            throw notFoundException;
        }

        CityEntity cityEntity = databaseService.getCityReference(id);

        if(city.getIdTara() != 0 && city.getNume() != null
            && (cityEntity.getIdTara() != city.getIdTara()
                || !cityEntity.getNume().equals(city.getNume()))
            && findCityByNameAndCountry(city.getIdTara(), city.getNume()).isPresent()) {
            throw dataConflictException;
        }

        if(city.getNume() != null && !cityEntity.getNume().equals(city.getNume())) {
            cityEntity.setNume(city.getNume());
        }

        if(city.getIdTara() != 0 && cityEntity.getIdTara() != city.getIdTara()){
            cityEntity.setIdTara(city.getIdTara());
        }

        if(city.getLat() != null && !cityEntity.getLat().equals(city.getLat())){
            cityEntity.setLat(city.getLat());
        }

        if(city.getLon() != null && !cityEntity.getLon().equals(city.getLon())){
            cityEntity.setLon(city.getLon());
        }

        cityList = databaseService.saveAndUpdate(cityEntity);
    }

    public void deleteCity(int id){
        if(id <= 0) {
            throw badRequestException;
        }

        if(getCityById(id).isEmpty()){
            throw notFoundException;
        }

        cityList = databaseService.deleteCityAndUpdate(id);
    }

    public Optional<City> findCityByNameAndCountry(int id_tara, String name){
        return cityList.stream()
                .filter(city -> city.getNume().equals(name) && city.getIdTara() == id_tara)
                .findFirst();
    }
}
