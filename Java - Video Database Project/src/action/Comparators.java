package action;

import actor.Actor;
import actor.ActorActionList;
import entertainment.VideoActionList;
import user.User;

/**
 * Aceasta clasa contine toti comparatorii folositi in sortari de liste.
 * @author Dutu Alin Calin
 */
public final class Comparators {
    /**
     * Se sorteaza ascendent 2 actori in functie de notele de evaluare in primul rand si in functie
     * de nume in al doilea rand.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int ascAverage(final ActorActionList actor1, final ActorActionList actor2) {
        if (actor1.getFinalRating() < actor2.getFinalRating()) {
            return -1;
        }

        if (actor1.getFinalRating() > actor2.getFinalRating()) {
            return 1;
        }

        return (actor1.getName().compareTo(actor2.getName()));
    }

    /**
     * Se sorteaza descendent 2 actori in functie de notele de evaluare in primul rand si in functie
     * de nume in al doilea rand.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int descAverage(final ActorActionList actor1, final ActorActionList actor2) {
        if (actor1.getFinalRating() > actor2.getFinalRating()) {
            return -1;
        }

        if (actor1.getFinalRating() < actor2.getFinalRating()) {
            return 1;
        }

        return (actor2.getName().compareTo(actor1.getName()));
    }

    /**
     * Se sorteaza descendent 2 actori in functie de numarul de premii in primul rand si in functie
     * de nume in al doilea rand.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int descAwards(final ActorActionList actor1, final ActorActionList actor2) {
        if (actor2.getNoAwards() < actor1.getNoAwards()) {
            return -1;
        }

        if (actor2.getNoAwards() > actor1.getNoAwards()) {
            return 1;
        }

        return (actor2.getName().compareTo(actor1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 actori in functie de numarul de premii in primul rand si in functie
     * de nume in al doilea rand.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int ascAwards(final ActorActionList actor1, final ActorActionList actor2) {
        if (actor2.getNoAwards() > actor1.getNoAwards()) {
            return -1;
        }

        if (actor2.getNoAwards() < actor1.getNoAwards()) {
            return 1;
        }

        return (actor1.getName().compareTo(actor2.getName()));
    }

    /**
     * Se sorteaza descendent 2 actori in functie de numarul de premii in primul rand si in functie
     * de nume in al doilea rand.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int descActorName(final Actor actor1, final Actor actor2) {
        return (actor2.getName().compareTo(actor1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 actori in functie de nume.
     *
     * @param actor1 Primul actor;
     * @param actor2 Al doilea actor;
     * @return Numar care determina daca cei doi actori trebuie interschimbati in lista.
     */
    public int ascActorName(final Actor actor1, final Actor actor2) {
        return (actor1.getName().compareTo(actor2.getName()));
    }

    /**
     * Se sorteaza descendent 2 videoclipuri in functie de notele de evaluare in primul rand si in
     * functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int descVideoRating(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getRating() < video1.getRating()) {
            return -1;
        }

        if (video2.getRating() > video1.getRating()) {
            return 1;
        }

        return (video2.getName().compareTo(video1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de notele de evaluare in primul rand si in
     * functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int ascVideoRating(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getRating() > video1.getRating()) {
            return -1;
        }

        if (video2.getRating() < video1.getRating()) {
            return 1;
        }

        return (video1.getName().compareTo(video2.getName()));
    }

    /**
     * Se sorteaza descendent 2 videoclipuri in functie de numarul de aparitii in listele de
     * videoclipuri favorite ale utilizatorilor in primul rand si in functie de nume in al doilea
     * rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int descVideoFavs(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getFavs() < video1.getFavs()) {
            return -1;
        }

        if (video2.getFavs() > video1.getFavs()) {
            return 1;
        }

        return (video2.getName().compareTo(video1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de numarul de aparitii in listele de
     * videoclipuri favorite ale utilizatorilor in primul rand si in functie de nume in al doilea
     * rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int ascVideoFavs(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getFavs() > video1.getFavs()) {
            return -1;
        }

        if (video2.getFavs() < video1.getFavs()) {
            return 1;
        }

        return (video1.getName().compareTo(video2.getName()));
    }

    /**
     * Se sorteaza descendent 2 videoclipuri in functie de durata videoclipului in primul rand si in
     * functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int descLength(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getLength() < video1.getLength()) {
            return -1;
        }

        if (video2.getLength() > video1.getLength()) {
            return 1;
        }

        return (video2.getName().compareTo(video1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de durata videoclipului in primul rand si in
     * functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int ascLength(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getLength() > video1.getLength()) {
            return -1;
        }

        if (video2.getLength() < video1.getLength()) {
            return 1;
        }

        return (video1.getName().compareTo(video2.getName()));
    }

    /**
     * Se sorteaza descendent 2 videoclipuri in functie de numarul de vizualizari in primul rand si
     * in functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int descViews(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getViews() < video1.getViews()) {
            return -1;
        }

        if (video2.getViews() > video1.getViews()) {
            return 1;
        }

        return (video2.getName().compareTo(video1.getName()));
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de numarul de vizualizari in primul rand si
     * in functie de nume in al doilea rand.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int ascViews(final VideoActionList video1, final VideoActionList video2) {
        if (video2.getViews() > video1.getViews()) {
            return -1;
        }

        if (video2.getViews() < video1.getViews()) {
            return 1;
        }

        return (video1.getName().compareTo(video2.getName()));
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de notele de evaluare.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int bestRating(final VideoActionList video1, final VideoActionList video2) {
        return (int) (video2.getRating() - video1.getRating());
    }

    /**
     * Se sorteaza ascendent 2 videoclipuri in functie de numarul de aparitii in listele de
     * videoclipuri favorite ale utilizatorilor.
     *
     * @param video1 Primul videoclip;
     * @param video2 Al doilea videoclip;
     * @return Numar care determina daca cele doua videoclipuri trebuie interschimbate in lista.
     */
    public int descFavs(final VideoActionList video1, final VideoActionList video2) {
        return Integer.compare(video2.getFavs(), video1.getFavs());
    }

    /**
     * Se sorteaza descendent in functie de popularitatea genului.
     *
     * @param genre1 Primul gen;
     * @param genre2 Al doilea gen;
     * @return umar care determina daca cele doua genuri trebuie interschimbate in lista.
     */
    public int mostPopular(final VideoActionList genre1, final VideoActionList genre2) {
        return Integer.compare(genre2.getViews(), genre1.getViews());
    }

    /**
     * Se sorteaza descendent 2 utilizatori in functie de numarul de evaluari.
     *
     * @param user1 Primul utilizator;
     * @param user2 Al doilea utilizator;
     * @return Numarul care determina daca cei doi utilizatori trebuie interschimbati in lista.
     */
    public int descUser(final User user1, final User user2) {
        if (user2.getNumberRatings() < user1.getNumberRatings()) {
            return -1;
        }

        if (user2.getNumberRatings() > user1.getNumberRatings()) {
            return 1;
        }

        return (user2.getUsername().compareTo(user1.getUsername()));
    }

    /**
     * Se sorteaza ascendent 2 utilizatori in functie de numarul de evaluari.
     *
     * @param user1 Primul utilizator;
     * @param user2 Al doilea utilizator;
     * @return Numarul care determina daca cei doi utilizatori trebuie interschimbati in lista.
     */
    public int ascUser(final User user1, final User user2) {
        if (user2.getNumberRatings() > user1.getNumberRatings()) {
            return -1;
        }

        if (user2.getNumberRatings() < user1.getNumberRatings()) {
            return 1;
        }

        return (user1.getUsername().compareTo(user2.getUsername()));
    }
}
