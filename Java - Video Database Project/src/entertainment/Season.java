package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;

    private final ArrayList<String> userRated;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        userRated = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }

    /**
     * Adauga utilizatorul in lista de evaluatori.
     *
     * @param name Numele utilizatorului;
     */
    public void addUserRated(final String name) {
        userRated.add(name);
    }

    /**
     * Verifica daca utilizatorul este in lista de evaluatori.
     *
     * @param name Numele utilizatorului;
     * @return Adevarat daca utilizatorul este in lista de evaluatori sau fals daca nu este.
     */
    public boolean checkUserRated(final String name) {
        int i;

        if (userRated.size() == 0) {
            return false;
        }

        for (i = 0; i < userRated.size(); i++) {
            if (userRated.get(i).equals(name)) {
                return true;
            }
        }

        return false;
    }
}

