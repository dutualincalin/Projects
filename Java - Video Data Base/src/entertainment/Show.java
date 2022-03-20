package entertainment;

import java.util.ArrayList;

/**
 * Clasa folosita pentru retinerea unui serial.
 * @author Dutu Alin Calin
 */
public final class Show extends Video {
    private final int numberSeasons;
    private final ArrayList<Season> seasons;

    public Show(final String name, final int year, final ArrayList<String> actors,
                final ArrayList<String> genres, final int numberSeasons,
                final ArrayList<Season> seasons) {
        super(name, year, genres, actors);
        this.numberSeasons = numberSeasons;
        this.seasons = seasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    /**
     * Calculeaza nota de evaluare medie a intregului serial ca media aritmetica a tuturor
     * sezoanelor. Nota de evaluare a unui sezon se calculeaza ca media artimetica a notelor pentru
     * acel sezon.
     *
     * @return
     */
    public double getRating() {
        int i, j, check = 0;
        double totalRating = 0, seasonRating;

        if (numberSeasons != 0) {

            for (i = 0; i < numberSeasons; i++) {
                seasonRating = 0;

                if (seasons.get(i).getRatings().size() != 0) {
                    check = 1;
                    for (j = 0; j < seasons.get(i).getRatings().size(); j++) {
                        seasonRating += seasons.get(i).getRatings().get(j);
                    }
                }

                totalRating += seasonRating;
            }

            if (check == 0) {
                return -1;
            }
        } else {
            return -1;
        }

        totalRating /= numberSeasons;
        return totalRating;
    }

    /**
     * Calculeaza durata intregului serial, adica suma duratelor sezoanelor.
     *
     * @return Durata serialului;
     */
    public int getwholeDuration() {
        if (seasons.size() == 0) {
            return 0;
        }

        int i, duration = 0;
        for (i = 0; i < numberSeasons; i++) {
            duration += seasons.get(i).getDuration();
        }

        return duration;
    }
}
