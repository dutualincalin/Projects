package com.tema2sprc.repository;

import com.tema2sprc.entity.TemperatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemperatureRepository extends JpaRepository<TemperatureEntity, Integer> {
}
