package com.weatherforecast.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orase",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "id_tara", "nume_oras" }) })
public class CityEntity {

    @Id
    @Column(name = "id")
    int id;

    @Column(name = "id_tara")
    @NonNull
    int idTara;

    @Column(name = "nume_oras")
    String nume;

    @Column(name = "latitudine")
    Double lat;

    @Column(name = "longitudine")
    Double lon;

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE,
        orphanRemoval = true,
        mappedBy = "idOras"
    )
    List<TemperatureEntity> temperatures;

    public CityEntity(int idTara, String nume, Double lat, Double lon) {
        this.idTara = idTara;
        this.nume = nume;
        this.lat = lat;
        this.lon = lon;
    }

    public CityEntity(int id, int idTara, String nume, Double lat, Double lon) {
        this.id = id;
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
        if (!(o instanceof CityEntity)) {
            return false;
        }

        CityEntity that = (CityEntity) o;

        if (getIdTara() != that.getIdTara()) {
            return false;
        }
        if (Double.compare(that.getLat(), getLat()) != 0) {
            return false;
        }
        if (Double.compare(that.getLon(), getLon()) != 0) {
            return false;
        }
        return getNume() != null ? getNume().equals(that.getNume()) :
                that.getNume() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getIdTara();
        result = 31 * result + (getNume() != null ? getNume().hashCode() : 0);
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
