package com.tema2sprc.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Country {
    int id;
    String nume;
    Double lat;
    Double lon;

    public Country(String nume, double lat, double lon) {
        this.nume = nume;
        this.lat = lat;
        this.lon = lon;
    }

    public Country(int id, String nume, double lat, double lon) {
        this.id = id;
        this.nume = nume;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }

        Country country = (Country) o;

        if (Double.compare(country.lat, lat) != 0) {
            return false;
        }
        if (Double.compare(country.lon, lon) != 0) {
            return false;
        }
        return Objects.equals(nume, country.nume);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nume != null ? nume.hashCode() : 0;
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
