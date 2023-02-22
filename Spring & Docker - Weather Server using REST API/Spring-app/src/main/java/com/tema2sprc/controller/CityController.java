package com.tema2sprc.controller;

import com.tema2sprc.pojo.City;
import com.tema2sprc.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {
    private final CityService service;

    public CityController(CityService service){
        this.service = service;
    }

    @PostMapping("/api/cities")
    public ResponseEntity<String> addNewCity(@Validated @RequestBody City city) {
        return ResponseEntity.status(201).body("{\"id\": " +  service.addNewCity(city) + "}");
    }

    @GetMapping("/api/cities")
    public ResponseEntity<List<City>> getAllCities(){
        return ResponseEntity.status(200).body(service.getCityList());
    }

    @GetMapping("/api/cities/country/{id_Tara}")
    public ResponseEntity<List<City>> getAllCitiesFromCountry(@PathVariable int id_Tara){
        return ResponseEntity.status(200).body(service.getCitiesByCountryId(id_Tara));
    }

    @PutMapping("/api/cities/{id}")
    public ResponseEntity<Void> modifyCity(@PathVariable int id, @Validated @RequestBody City city){
        service.modifyCity(id, city);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/api/cities/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable int id){
        service.deleteCity(id);
        return ResponseEntity.status(200).build();
    }
}
