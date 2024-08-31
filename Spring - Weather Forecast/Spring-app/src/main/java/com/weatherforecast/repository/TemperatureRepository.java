package com.weatherforecast.repository;

import com.weatherforecast.entity.TemperatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<TemperatureEntity, Integer> {
}
