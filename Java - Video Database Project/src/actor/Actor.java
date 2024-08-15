package actor;

import java.util.ArrayList;
import java.util.Map;

/**
 * Clasa folosita pentru retinerea unui actor
 * @author Dutu Alin Calin
 */
public final class Actor {
    private String name;
    private final String careerDescription;
    private ArrayList<String> filmography;
    private Map<ActorsAwards, Integer> awards;


    public Actor(final String name, final String careerDescription,
                 final ArrayList<String> filmography, final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public void setAwards(final Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }
}
