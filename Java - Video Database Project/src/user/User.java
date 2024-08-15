package user;

import java.util.ArrayList;
import java.util.Map;

/**
 * Clasa folosita pentru retinerea unui utilizator
 * @author Dutu Alin Calin
 */
public final class User {
    private String username;
    private String subscription;
    private Map<String, Integer> history;
    private ArrayList<String> favourite;
    private int numberRatings;

    public User(final String name, final String sub, final Map<String, Integer> history,
                final ArrayList<String> favourite) {
        username = name;
        subscription = sub;
        this.history = history;
        this.favourite = favourite;
        numberRatings = 0;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setSubscription(final String subscription) {
        this.subscription = subscription;
    }

    public void setHistory(final Map<String, Integer> history) {
        this.history = history;
    }

    public void setFavourite(final ArrayList<String> favourite) {
        this.favourite = favourite;
    }

    /**
     * Incrementeaza numarul de evaluari.
     */
    public void addRating() {
        this.numberRatings += 1;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscription() {
        return subscription;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavourite() {
        return favourite;
    }

    public int getNumberRatings() {
        return numberRatings;
    }
}
