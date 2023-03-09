package com.tema2sprc.service;

import com.tema2sprc.entity.CountryEntity;
import com.tema2sprc.exceptions.BadRequestException;
import com.tema2sprc.exceptions.DataConflictException;
import com.tema2sprc.exceptions.NotFoundException;
import com.tema2sprc.pojo.Country;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    private int entityId;
    private List<Country> countryList;

    private final DatabaseService databaseService;

    private final DataConflictException dataConflictException;

    private final NotFoundException notFoundException;

    private final BadRequestException badRequestException;

    public CountryService(DatabaseService databaseService,
                          DataConflictException dataConflictException,
                          NotFoundException notFoundException,
                          BadRequestException badRequestException) {
        this.databaseService = databaseService;
        this.dataConflictException = dataConflictException;
        this.notFoundException = notFoundException;
        countryList = databaseService.getCountryData();
        this.badRequestException = badRequestException;
        entityId = 1;
    }

    @Transactional
    public int addNewCountry(Country country) {
        if(country.getNume() == null || country.getLat() == null || country.getLon() == null){
            throw badRequestException;
        }

        if(findCountryByName(country.getNume()).isPresent()){
            throw dataConflictException;
        }

        country.setId(entityId);
        entityId++;
        countryList.add(country);
        databaseService.addTo(country);

        return country.getId();
    }

    public List<Country> getCountryList(){
        return countryList;
    }

    public void modifyCountry (int id, Country country) {
        if(id <= 0 || country.getId() != id){
            throw badRequestException;
        }

        if(findCountryById(id).isEmpty()){
            throw notFoundException;
        }

        CountryEntity countryEntity = databaseService.getCountryReference(id);

        if(country.getNume() != null && !country.getNume().equals(countryEntity.getNume())) {
            if (findCountryByName(country.getNume()).isPresent()) {
                throw dataConflictException;
            }

            countryEntity.setNume(country.getNume());
        }

        if(country.getLat() != null && !country.getLat().equals(countryEntity.getLat())) {
            countryEntity.setLat(country.getLat());
        }

        if(country.getLon() != null && !country.getLon().equals(countryEntity.getLon())) {
            countryEntity.setLon(country.getLon());
        }

        countryList = databaseService.saveAndUpdate(countryEntity);
    }

    public void deleteCountry(int id) {
        if(id <= 0) {
            throw badRequestException;
        }

        if(findCountryById(id).isEmpty()) {
            throw notFoundException;
        }

        countryList = databaseService.deleteCountryAndUpdate(id);
    }

    public Optional<Country> findCountryById(int id) {
        return countryList.stream()
                .filter(country -> country.getId() == id)
                .findFirst();
    }

    public Optional<Country> findCountryByName(String name){
        return countryList.stream()
                .filter(country -> country.getNume().equals(name))
                .findFirst();
    }
}
