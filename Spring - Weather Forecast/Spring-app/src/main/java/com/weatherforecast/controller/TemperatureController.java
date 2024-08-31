package com.weatherforecast.controller;

import com.weatherforecast.pojo.Temperature;
import com.weatherforecast.pojo.TemperatureOutput;
import com.weatherforecast.service.TemperatureService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class TemperatureController {
    private final TemperatureService service;

    public TemperatureController(TemperatureService service) {
        this.service = service;
    }

    @PostMapping("/api/temperatures")
    public ResponseEntity<String> addTemperature(@Validated @RequestBody Temperature temperature) {
        return ResponseEntity.status(201).body("{\"id\": " +  service.addNewTemperature(temperature) + "}");
    }

    @GetMapping("/api/temperatures")
    public ResponseEntity<List<TemperatureOutput>> getTemperaturesByCoordinatesAndDate(
        @RequestParam(required = false) Double lat,
        @RequestParam(required = false) Double lon,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date until
    ){
        return ResponseEntity.status(200).body(
            service.getTemperaturesByCoordinatesAndDate(lat, lon, from, until)
        );
    }

    @GetMapping("api/temperatures/cities/{id_oras}")
    public ResponseEntity<List<TemperatureOutput>> getTemperaturesByCityAndDate(
        @PathVariable int id_oras,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date until
    ){
        return ResponseEntity.status(200).body(
            service.getTemperaturesByCityAndDate(id_oras, from, until)
        );
    }

    @GetMapping("api/temperatures/countries/{id_tara}")
    public ResponseEntity<List<TemperatureOutput>> getTemperaturesByCountryAndDate(
        @PathVariable int id_tara,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date until
    ){
        return ResponseEntity.status(200).body(
            service.getTemperaturesByCountryAndDate(id_tara, from, until)
        );
    }

    @PutMapping("/api/temperatures/{id}")
    public ResponseEntity<Void> modifyTemperature(@PathVariable int id, @Validated @RequestBody Temperature temperature) {
        service.modifyTemperature(id, temperature);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/api/temperatures/{id}")
    public ResponseEntity<Void> deleteTemperature(@PathVariable int id) {
        service.deleteTemperature(id);
        return ResponseEntity.status(200).build();
    }
}
