package entertainment;

/**
 * Clasa folosita pentru crearea unor liste care urmeaza a fi sortate in functie de unul din
 * campurile clasei.
 * @author Dutu Alin Calin
 */
public final class VideoActionList {
    private String name;
    private double rating;
    private int favs = 0;
    private int length;
    private int views = 0;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public int getFavs() {
        return favs;
    }

    /**
     * Adauga o aparitie a videoclipului intr-o lista de favorite.
     */
    public void incFavs() {
        this.favs += 1;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public int getViews() {
        return views;
    }

    /**
     * Adauga vizualizari ale videoclipului.
     * @param newViews Noi Vizualizari;
     */
    public void addViews(final int newViews) {
        this.views += newViews;
    }
}
