package com.weatherforecast.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TemperatureOutput {
    int id;

    Double valoare;

    Date timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemperatureOutput)) {
            return false;
        }

        TemperatureOutput that = (TemperatureOutput) o;

        if (getId() != that.getId()) {
            return false;
        }
        if (getValoare() != null ? !getValoare().equals(that.getValoare()) :
                that.getValoare() != null) {
            return false;
        }
        return getTimestamp() != null ? getTimestamp().equals(that.getTimestamp()) :
                that.getTimestamp() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getValoare() != null ? getValoare().hashCode() : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }
}
