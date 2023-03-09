package com.tema2sprc.mapper;

import com.tema2sprc.entity.CityEntity;
import com.tema2sprc.entity.CountryEntity;
import com.tema2sprc.entity.TemperatureEntity;
import com.tema2sprc.pojo.City;
import com.tema2sprc.pojo.Country;
import com.tema2sprc.pojo.Temperature;
import com.tema2sprc.pojo.TemperatureOutput;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
