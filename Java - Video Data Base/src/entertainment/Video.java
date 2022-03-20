package entertainment;

import java.util.ArrayList;

/**
 * Clasa folosita pentru a retine un video.
 * @author Dutu Alin Calin
 */
public class Video {
    private String name;
    private int year;
    private ArrayList<String> genres;
    private ArrayList<String> actors;


    public Video(final String name, final int year, final ArrayList<String> genres,
                 final ArrayList<String> actors) {
        this.name = name;
        this.year = year;
        this.genres = genres;
        this.actors = actors;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final void setYear(final int year) {
        this.year = year;
    }

    public final void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public final void setActors(final ArrayList<String> actors) {
        this.actors = actors;
    }

    public final String getName() {
        return name;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final ArrayList<String> getActors() {
        return actors;
    }
}
