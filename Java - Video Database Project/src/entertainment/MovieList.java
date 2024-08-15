package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa folosita pentru retinerea bazei de date a filmelor.
 * @author Dutu Alin Calin
 */
public final class MovieList {
    private final ArrayList<Movie> movies;

    public MovieList() {
        movies = new ArrayList<>();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    /**
     * Preia datele din clasa de input si le depoziteaza in aceasta clasa.
     *
     * @param moviesData Baza de date a filmelor din clasa de input;
     */
    public MovieList(final List<MovieInputData> moviesData) {
        this();
        int i;

        for (i = 0; i < moviesData.size(); i++) {
            Movie movie = new Movie(moviesData.get(i).getTitle(), moviesData.get(i).getYear(),
                    moviesData.get(i).getCast(), moviesData.get(i).getGenres(),
                    moviesData.get(i).getDuration());
            movies.add(movie);
        }
    }
}

