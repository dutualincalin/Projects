package actor;

/**
 * Clasa folosita pentru crearea unei liste filtrate ce retine anumiti parametrii necesari pentru
 * sortare.
 * @author Dutu Alin Calin
 */
public final class ActorActionList {
    private String name;
    private double rating;
    private int numberRatings;
    private int noAwards = 0;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    /**
     * Se efectueaza o suma de note de evaluare
     *
     * @param grade Nota de evaluare
     */
    public void incRating(final double grade) {
        rating += grade;
    }

    public double getRating() {
        return rating;
    }

    /**
     * Se seteaza numarul de evaluari dat.
     *
     * @param numberRatings Numarul de evaluari;
     */
    public void setNumberRatings(final int numberRatings) {
        this.numberRatings = numberRatings;
    }

    /**
     * Se aduna numarul de evaluari cu cel dat.
     *
     * @param numberGrades Numarul de evaluari;
     */
    public void incNumberRatings(final int numberGrades) {
        numberRatings += numberGrades;
    }

    /**
     * Se face o nota finala (media aritmetica a notelor).
     *
     * @return Nota medie finala;
     */
    public double getFinalRating() {
        return (rating / numberRatings);
    }

    /**
     * Se adauga numarul de premii.
     *
     * @param number Numarul de premii;
     */
    public void addNoAwards(final int number) {
        noAwards += number;
    }

    public int getNoAwards() {
        return noAwards;
    }
}
