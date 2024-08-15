package actor;

import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa care retine baza de date a actorilor
 * @author Dutu Alin Calin
 */
public final class ActorList {
    private final ArrayList<Actor> actors;

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ActorList() {
        actors = new ArrayList<>();
    }

    /**
     * Preia datele din clasa de input si le depoziteaza in aceasta clasa.
     *
     * @param actorsData Baza de date a actorilor din clasa de input;
     */
    public ActorList(final List<ActorInputData> actorsData) {
        this();
        int i;

        for (i = 0; i < actorsData.size(); i++) {
            Actor actor =
                    new Actor(actorsData.get(i).getName(), actorsData.get(i).getCareerDescription(),
                            actorsData.get(i).getFilmography(), actorsData.get(i).getAwards());
            actors.add(actor);
        }

    }
}
