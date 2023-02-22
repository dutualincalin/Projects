package com.tema2sprc.controller;

import com.tema2sprc.pojo.Country;
import com.tema2sprc.service.CountryService;
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
public class CountryController {
    private final CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @PostMapping(value = "/api/countries", produces = "application/json")
    public ResponseEntity<String> addNewCountry(@Validated @RequestBody Country country){
        return ResponseEntity.status(201).body("{\"id\": " +  service.addNewCountry(country) + "}");
    }

    @GetMapping("/api/countries")
    public ResponseEntity<List<Country>> getCountries(){

        return ResponseEntity.status(200).body(service.getCountryList());
    }

    @PutMapping("/api/countries/{id}")
    public ResponseEntity<Void> modifyCountry(@PathVariable int id, @Validated @RequestBody Country country){
        service.modifyCountry(id, country);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/api/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id) {
        service.deleteCountry(id);
        return ResponseEntity.status(200).build();
    }

}
