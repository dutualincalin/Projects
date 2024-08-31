package com.weatherforecast.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class City {
    int id;
    int idTara;
    String nume;
    Double lat;
    Double lon;

    public City(int idTara, String nume, Double lat, Double lon) {
        this.idTara = idTara;
        this.nume = nume;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }

        City city = (City) o;

        if (idTara != city.idTara) {
            return false;
        }
        if (Double.compare(city.lat, lat) != 0) {
            return false;
        }
        if (Double.compare(city.lon, lon) != 0) {
            return false;
        }
        return Objects.equals(nume, city.nume);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idTara;
        result = 31 * result + (nume != null ? nume.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
