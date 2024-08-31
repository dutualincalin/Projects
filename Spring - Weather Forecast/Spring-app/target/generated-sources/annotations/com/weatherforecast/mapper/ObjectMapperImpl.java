package com.weatherforecast.mapper;

import com.weatherforecast.entity.CityEntity;
import com.weatherforecast.entity.CountryEntity;
import com.weatherforecast.entity.TemperatureEntity;
import com.weatherforecast.pojo.City;
import com.weatherforecast.pojo.Country;
import com.weatherforecast.pojo.Temperature;
import com.weatherforecast.pojo.TemperatureOutput;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T00:28:01+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.11 (Eclipse Adoptium)"
)
@Component
public class ObjectMapperImpl implements ObjectMapper {

    @Override
    public City EntityToInstance(CityEntity entity) {
        if ( entity == null ) {
            return null;
        }

        City city = new City();

        city.setId( entity.getId() );
        city.setIdTara( entity.getIdTara() );
        city.setNume( entity.getNume() );
        city.setLat( entity.getLat() );
        city.setLon( entity.getLon() );

        return city;
    }

    @Override
    public CityEntity InstanceToEntity(City instance) {
        if ( instance == null ) {
            return null;
        }

        CityEntity cityEntity = new CityEntity();

        cityEntity.setId( instance.getId() );
        cityEntity.setIdTara( instance.getIdTara() );
        cityEntity.setNume( instance.getNume() );
        cityEntity.setLat( instance.getLat() );
        cityEntity.setLon( instance.getLon() );

        return cityEntity;
    }

    @Override
    public Country EntityToInstance(CountryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Country country = new Country();

        country.setId( entity.getId() );
        country.setNume( entity.getNume() );
        country.setLat( entity.getLat() );
        country.setLon( entity.getLon() );

        return country;
    }

    @Override
    public CountryEntity InstanceToEntity(Country instance) {
        if ( instance == null ) {
            return null;
        }

        CountryEntity countryEntity = new CountryEntity();

        countryEntity.setId( instance.getId() );
        countryEntity.setNume( instance.getNume() );
        countryEntity.setLat( instance.getLat() );
        countryEntity.setLon( instance.getLon() );

        return countryEntity;
    }

    @Override
    public Temperature EntityToInstance(TemperatureEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Temperature temperature = new Temperature();

        temperature.setId( entity.getId() );
        temperature.setValoare( entity.getValoare() );
        temperature.setTimestamp( entity.getTimestamp() );
        temperature.setIdOras( entity.getIdOras() );

        return temperature;
    }

    @Override
    public TemperatureEntity InstanceToEntity(Temperature instance) {
        if ( instance == null ) {
            return null;
        }

        TemperatureEntity temperatureEntity = new TemperatureEntity();

        temperatureEntity.setId( instance.getId() );
        temperatureEntity.setValoare( instance.getValoare() );
        temperatureEntity.setTimestamp( instance.getTimestamp() );
        temperatureEntity.setIdOras( instance.getIdOras() );

        return temperatureEntity;
    }

    @Override
    public TemperatureOutput InstanceToOutput(Temperature instance) {
        if ( instance == null ) {
            return null;
        }

        int id = 0;
        Double valoare = null;
        Date timestamp = null;

        id = instance.getId();
        valoare = instance.getValoare();
        timestamp = instance.getTimestamp();

        TemperatureOutput temperatureOutput = new TemperatureOutput( id, valoare, timestamp );

        return temperatureOutput;
    }
}
