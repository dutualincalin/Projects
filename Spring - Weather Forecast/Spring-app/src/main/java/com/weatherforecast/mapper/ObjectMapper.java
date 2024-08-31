package com.weatherforecast.mapper;

import com.weatherforecast.entity.CityEntity;
import com.weatherforecast.entity.CountryEntity;
import com.weatherforecast.entity.TemperatureEntity;
import com.weatherforecast.pojo.City;
import com.weatherforecast.pojo.Country;
import com.weatherforecast.pojo.Temperature;
import com.weatherforecast.pojo.TemperatureOutput;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ObjectMapper {
    ObjectMapper instance = Mappers.getMapper(ObjectMapper.class);

    City EntityToInstance(CityEntity entity);

    CityEntity InstanceToEntity(City instance);

    Country EntityToInstance(CountryEntity entity);

    CountryEntity InstanceToEntity(Country instance);

    Temperature EntityToInstance(TemperatureEntity entity);

    TemperatureEntity InstanceToEntity(Temperature instance);

    TemperatureOutput InstanceToOutput(Temperature instance);
}
