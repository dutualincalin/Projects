package com.weatherforecast.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Temperature {
    int id;
    Double valoare;
    Date timestamp;
    int idOras;

    public Temperature(Double valoare, Date timestamp, int idOras) {
        this.valoare = valoare;
        this.timestamp = timestamp;
        this.idOras = idOras;
    }

    public Temperature(int id, Double valoare, Date timestamp, int idOras) {
        this.id = id;
        this.valoare = valoare;
        this.timestamp = timestamp;
        this.idOras = idOras;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temperature)) {
            return false;
        }

        Temperature that = (Temperature) o;

        if (Double.compare(that.valoare, valoare) != 0) {
            return false;
        }
        if (idOras != that.idOras) {
            return false;
        }
        return Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(valoare);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + idOras;
        return result;
    }
}
