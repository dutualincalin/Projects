package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa folosita pentru retinerea unui film.
 * @author Dutu Alin Calin
 */
public final class Movie extends Video {
    private int duration;
    private final List<Double> rating;
    private final ArrayList<String> userRated;

    public Movie(final String title, final int year, final ArrayList<String> actors,
                 final ArrayList<String> genres, final int duration) {
        super(title, year, genres, actors);
        this.duration = duration;
        rating = new ArrayList<>();
        userRated = new ArrayList<>();
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    /**
     * Se returneaza durata filmului.
     *
     * @return Durata filmului;
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Calculeaza nota de evaluare medie ca media aritmetica a notelor din lista rating.
     *
     * @return Nota medie de evaluare;
     */
    public double getRating() {
        int i;
        double total = 0;

        if (rating.size() == 0) {
            return -1;
        }

        for (i = 0; i < rating.size(); i++) {
            total += rating.get(i);
        }

        total = total / rating.size();
        return total;
    }

    /**
     * Adauga o noua nota de evaluare in lista.
     *
     * @param newRating Noua nota de evaluare;
     */
    public void addRating(final double newRating) {
        rating.add(newRating);
    }

    /**
     * Adauga numele utilizatorului in lista celor care au evaluat filmul.
     *
     * @param name Numele utilizatorului;
     */
    public void addUserRated(final String name) {
        userRated.add(name);
    }

    /**
     * Cauta in lista daca utilizatorul a evaluat filmul.
     *
     * @param name Numele utilizatorului;
     * @return Adevarat daca utilizatorul a evaluat filmul sau fals in caz contrar;
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
