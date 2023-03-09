package com.tema2sprc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "temperaturi",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "id_oras", "timestamp" }) })
public class TemperatureEntity {
    @Id
    @Column(name = "id")
    int id;

    @Column(name = "valoare")
    Double valoare;

    @Column(name = "timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    @Column(name = "id_oras")
    @NonNull
    int idOras;

    public TemperatureEntity(Double valoare, int idOras) {
        this.valoare = valoare;
        this.idOras = idOras;
    }

    public TemperatureEntity(int id, int idOras, Double valoare, Date timestamp) {
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
        if (!(o instanceof TemperatureEntity)) {
            return false;
        }

        TemperatureEntity that = (TemperatureEntity) o;

        if (getId() != that.getId()) {
            return false;
        }
        if (Double.compare(that.getValoare(), getValoare()) != 0) {
            return false;
        }
        if (getIdOras() != that.getIdOras()) {
            return false;
        }
        return getTimestamp() != null ? getTimestamp().equals(that.getTimestamp()) :
                that.getTimestamp() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getId();
        temp = Double.doubleToLongBits(getValoare());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        result = 31 * result + getIdOras();
        return result;
    }
}
