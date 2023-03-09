package com.tema2sprc.entity;

import com.tema2sprc.pojo.Temperature;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tari")
public class CountryEntity {
    @Id
    @Column(name = "id")
    int id;

    @Column(name = "nume_tara", unique = true)
    String nume;

    @Column(name = "latitudine")
    Double lat;

    @Column(name = "longitudine")
    Double lon;


    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = CascadeType.REMOVE,
        orphanRemoval = true,
        mappedBy = "idTara"
    )
    List<CityEntity> cities;

    public CountryEntity(int id, String nume, Double lat, Double longtudine){
        this.id = id;
        this.nume = nume;
        this.lat = lat;
        this.lon = longtudine;
    }

    public CountryEntity(String nume, Double lat, Double longtudine) {
        this.nume = nume;
        this.lat = lat;
        this.lon = longtudine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryEntity)) {
            return false;
        }

        CountryEntity that = (CountryEntity) o;

        if (getId() != that.getId()) {
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
        result = getId();
        result = 31 * result + (getNume() != null ? getNume().hashCode() : 0);
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
