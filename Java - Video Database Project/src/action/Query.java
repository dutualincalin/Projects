package action;

import actor.Actor;
import actor.ActorActionList;
import entertainment.Movie;
import entertainment.Show;
import entertainment.VideoActionList;
import user.User;
import utils.Result;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Aceasta clasa este folosita pentru executarea tuturor comenzilor.
 * @author Dutu Alin Calin
 */
public final class Query {
    private int i, j, k;
    private final Result result = new Result();
    private final StringBuilder helper = new StringBuilder();
    private final Comparators comparator = new Comparators();


    /**
     * Aceasta functie ia fiecare videoclip de tip object la rand si verifica daca respecta filtrele
     * date. Daca un videoclip respecta filtrele, atunci numele acestuia va fi adaugat in lista. La
     * final se va returna lista filtrata.
     *
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param years Anul dupa care se filtreaza videoclipurile;
     * @param genres Genurile dupa care se filtreaza videoclipurile;
     * @param object Tipul de obiect care se filtreaza (Filme / Seriale);
     * @return O structura de tip VideoActionList care va avea toate tipurile de videoclipuri de tip
     * object care indeplinesc conditiile date de filtre.
     */
    public ArrayList<VideoActionList> createActionList(final ArrayList<Movie> movies,
                                                       final ArrayList<Show> shows,
                                                       final List<String> years,
                                                       final List<String> genres,
                                                       final String object) {

        int nrFilters;
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;
        String yearHelper;

        if (object.equals("movies")) {

            for (i = 0; i < movies.size(); i++) {

                nrFilters = 0;
                yearHelper = "" + movies.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < movies.get(i).getGenres().size(); k++) {
                            if (movies.get(i).getGenres().get(k).equals(genres.get(j))) {
                                nrFilters++;
                                break;
                            }

                            if (genres.get(j) == null) {
                                nrFilters = 1;
                            }
                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(movies.get(i).getName());
                    videoList.add(video);
                }
            }
        } else {
            for (i = 0; i < shows.size(); i++) {

                nrFilters = 0;
                yearHelper = "" + shows.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < shows.get(i).getGenres().size(); k++) {
                            if (shows.get(i).getGenres().get(k).equals(genres.get(j))) {
                                nrFilters++;
                                break;
                            }

                            if (genres.get(j) == null) {
                                nrFilters = 1;
                            }
                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(shows.get(i).getName());
                    videoList.add(video);
                }
            }
        }

        return videoList;
    }

    /**
     * Aceasta functie itereaza prin toate videoclipurile si introduce intr-o lista de tip
     * ActorActionList numele actorilor, numarul de aparitii si suma notelor de evaluare ale
     * videoclipurilor cu un rating mai mare decat 0. Se face o sortare a rating-urilor finale ale
     * actorilor in functie de tipul de sortare si apoi se adauga numele actorilor ca mesaj in
     * structura de result. In final, se returneaza rezultatul.
     *
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param sortType Tipul de sortare;
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result average(final ArrayList<Movie> movies, final ArrayList<Show> shows,
                          final String sortType, final int number, final int actionId) {
        result.setId(actionId);

        int actorIter, found;
        ArrayList<ActorActionList> listActors = new ArrayList<>();
        ActorActionList actor;

        for (i = 0; i < movies.size(); i++) {
            if (movies.get(i).getRating() > 0) {
                for (actorIter = 0; actorIter < movies.get(i).getActors().size(); actorIter++) {
                    if (listActors.size() == 0) {
                        actor = new ActorActionList();
                        actor.setName(movies.get(i).getActors().get(actorIter));
                        actor.setRating(movies.get(i).getRating());
                        actor.setNumberRatings(1);
                        listActors.add(actor);
                        continue;
                    }

                    found = 0;
                    for (j = 0; j < listActors.size(); j++) {
                        if (movies.get(i).getActors().get(actorIter)
                                .equals(listActors.get(j).getName())) {
                            listActors.get(j).incRating(movies.get(i).getRating());
                            listActors.get(j).incNumberRatings(1);
                            found = 1;
                            break;
                        }
                    }

                    if (found == 0) {
                        actor = new ActorActionList();
                        actor.setName(movies.get(i).getActors().get(actorIter));
                        actor.setRating(movies.get(i).getRating());
                        actor.setNumberRatings(1);
                        listActors.add(actor);
                    }
                }
            }
        }

        for (i = 0; i < shows.size(); i++) {
            if (shows.get(i).getRating() > 0) {
                for (actorIter = 0; actorIter < shows.get(i).getActors().size(); actorIter++) {
                    if (listActors.size() == 0) {
                        actor = new ActorActionList();
                        actor.setName(shows.get(i).getActors().get(actorIter));
                        actor.setRating(shows.get(i).getRating());
                        actor.setNumberRatings(1);
                        listActors.add(actor);
                        continue;
                    }

                    found = 0;
                    for (j = 0; j < listActors.size(); j++) {
                        if (shows.get(i).getActors().get(actorIter)
                                .equals(listActors.get(j).getName())) {
                            listActors.get(j).incRating(shows.get(i).getRating());
                            listActors.get(j).incNumberRatings(1);
                            found = 1;
                            break;
                        }
                    }

                    if (found == 0) {
                        actor = new ActorActionList();
                        actor.setName(shows.get(i).getActors().get(actorIter));
                        actor.setRating(shows.get(i).getRating());
                        actor.setNumberRatings(1);
                        listActors.add(actor);
                    }

                }
            }
        }

        if (sortType.equals("asc")) {
            listActors.sort(comparator::ascAverage);
        } else {
            listActors.sort(comparator::descAverage);
        }

        if (number < listActors.size()) {
            for (i = 0; i < (number - 1); i++) {
                helper.append(listActors.get(i).getName());
                helper.append(", ");
            }

            helper.append(listActors.get(number - 1).getName());
        } else {
            for (i = 0; i < (listActors.size() - 1); i++) {
                helper.append(listActors.get(i).getName());
                helper.append(", ");
            }

            helper.append(listActors.get(listActors.size() - 1).getName());
        }
        result.setMessage("Query result: [" + helper + "]");
        return result;
    }

    /**
     * Aceasta functie filtreaza lista de actori in functie de *categoriile* mentionate de filtre.
     * Pentru fiecare actor se va calcula numarul total de premii. Se sorteaza actorii in functie
     * de numarul de premii si nume in ordinea data de sortType si se adauga in rezultat numele
     * actorilor. In final se returneaza rezultatul.
     *
     * @param actors Baza de date a actorilor;
     * @param awards Premiile dupa care se filtreaza actorii;
     * @param sortType Tipul de sortare;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result awards(final ArrayList<Actor> actors, final List<String> awards,
                         final String sortType, final int actionId) {
        result.setId(actionId);

        if (actors.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        ArrayList<ActorActionList> finalList = new ArrayList<>();
        ActorActionList actorNode;
        int nrAwards, nrAwardsCat;

        for (i = 0; i < actors.size(); i++) {
            if (actors.get(i).getAwards().size() == 0) {
                continue;
            }

            actorNode = new ActorActionList();
            nrAwardsCat = 0;

            for (j = 0; j < awards.size(); j++) {
                if (actors.get(i).getAwards()
                        .containsKey(Utils.stringToAwards(awards.get(j).toUpperCase()))) {
                    nrAwardsCat++;
                }
            }

            if (nrAwardsCat == awards.size()) {

                nrAwards = 0;

                for (int countAwards : actors.get(i).getAwards().values()) {
                    nrAwards += countAwards;
                }

                actorNode.addNoAwards(nrAwards);
                actorNode.setName(actors.get(i).getName());
                finalList.add(actorNode);
            }
        }

        if (finalList.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        if (sortType.equals("asc")) {
            finalList.sort(comparator::ascAwards);
        } else {
            finalList.sort(comparator::descAwards);
        }

        for (i = 0; i < finalList.size() - 1; i++) {
            helper.append(finalList.get(i).getName());
            helper.append(", ");
        }

        helper.append(finalList.get(finalList.size() - 1).getName());
        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     * Functia sorteaza lista de actori si apoi filtreaza actorii care au toate cuvintele date ca
     * parametru din lista words in descrierea carierei lor( Descrierea se imparte in cuvinte, iar
     * aceste cuvinte sunt verificate daca exista in words). In final se salveaza in rezultat lista
     * actorilor care au trecut cu succes prin criteriile stabilite si se transmite structura de tip
     * Result.
     *
     * @param actors Baza de date a actorilor;
     * @param words Cuvintele cu care se filtreaza actorii in functie de descrierea lor;
     * @param sortType Tipul de sortare;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result description(final ArrayList<Actor> actors, final List<String> words,
                              final String sortType, final int actionId) {
        result.setId(actionId);
        String[] stringHelper;

        if (actors.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        ArrayList<String> finalList = new ArrayList<>();
        int nrWords;

        if (sortType.equals("asc")) {
            actors.sort(comparator::ascActorName);
        } else {
            actors.sort(comparator::descActorName);
        }

        for (i = 0; i < actors.size(); i++) {
            if (actors.get(i).getCareerDescription().length() == 0) {
                continue;
            }

            nrWords = 0;
            stringHelper = actors.get(i).getCareerDescription().split("[, ?!./'\"@-]+");

            for (j = 0; j < words.size(); j++) {
                for (k = 0; k < stringHelper.length; k++) {
                    if (stringHelper[k].toLowerCase().equals(words.get(j).toLowerCase())) {
                        nrWords++;
                        break;
                    }
                }

            }

            if (nrWords == words.size()) {
                finalList.add(actors.get(i).getName());
            }
        }

        if (finalList.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        for (i = 0; i < finalList.size() - 1; i++) {
            helper.append(finalList.get(i));
            helper.append(", ");
        }

        helper.append(finalList.get(finalList.size() - 1));
        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     *  Aceasta functie filtreaza videoclipurile de tip object in functie de an, genuri si de nota
     *  de evaluare care trebuie sa fie mai mare decat 0. Urmeaza sa se sorteze lista filtrata in
     *  functie de nota de evaluare in ordinea data de sortType. Rezultatul va fi adaugat ca mesaj
     *  in structura result, urmand ca ca structura sa fie returnata.
     *
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param years Anul dupa care se filtreaza videoclipurile;
     * @param genres Genurile dupa care se filtreaza videoclipurile;
     * @param sortType Tipul de sortare;
     * @param object Tipul de obiect care se filtreaza (Filme / Seriale);
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result ratings(final ArrayList<Movie> movies, final ArrayList<Show> shows,
                          final List<String> years, final List<String> genres,
                          final String sortType, final String object, final int number,
                          final int actionId) {
        result.setId(actionId);

        int nrFilters;
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;
        String yearHelper;

        if (object.equals("movies")) {
            if (movies.size() == 0) {
                result.setMessage("Query result: []");
                return result;
            }

            for (i = 0; i < movies.size(); i++) {

                if (movies.get(i).getRating() == -1) {
                    continue;
                }

                nrFilters = 0;
                yearHelper = "" + movies.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < movies.get(i).getGenres().size(); k++) {
                            if (movies.get(i).getGenres().get(k).equals(genres.get(j))
                                    || genres.get(j) == null) {
                                nrFilters++;
                                break;
                            }
                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(movies.get(i).getName());
                    video.setRating(movies.get(i).getRating());
                    videoList.add(video);
                }
            }
        } else {
            if (shows.size() == 0) {
                result.setMessage("Query result: []");
                return result;
            }

            for (i = 0; i < shows.size(); i++) {
                if (shows.get(i).getRating() == -1) {
                    continue;
                }

                nrFilters = 0;
                yearHelper = "" + shows.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < shows.get(i).getGenres().size(); k++) {
                            if (shows.get(i).getGenres().get(k).equals(genres.get(j))
                                    || genres.get(j) == null) {
                                nrFilters++;
                                break;
                            }
                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(shows.get(i).getName());
                    video.setRating(shows.get(i).getRating());
                    videoList.add(video);
                }
            }
        }

        if (videoList.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        if (sortType.equals("asc")) {
            videoList.sort(comparator::ascVideoRating);
        } else {
            videoList.sort(comparator::descVideoRating);
        }

        if (videoList.size() < number) {
            for (i = 0; i < videoList.size() - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(videoList.size() - 1).getName());
        } else {
            for (i = 0; i < number - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(number - 1).getName());
        }

        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     * Functia filtreaza videoclipurile de tip object in functie de an, genuri folosind functia
     * createActionList, adauga numarul de aparitii in lista de favorite iterand prin lista de
     * videoclipuri si in lista de utilizatori, elimina videoclipurile care nu sunt incluse macar
     * intr-o singura lista de favorite. In final se adauga lista final in rezultat si se returneaza
     * rezultatul final.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param years Anul dupa care se filtreaza videoclipurile;
     * @param genres Genurile dupa care se filtreaza videoclipurile;
     * @param sortType Tipul de sortare;
     * @param object Tipul de obiect care se filtreaza (Filme / Seriale);
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result favorite(final ArrayList<User> users, final ArrayList<Movie> movies,
                           final ArrayList<Show> shows, final List<String> years,
                           final List<String> genres, final String sortType, final String object,
                           final int number, final int actionId) {
        result.setId(actionId);

        ArrayList<VideoActionList> videoList;

        videoList = createActionList(movies, shows, years, genres, object);

        if (videoList.size() == 0 || users.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        for (i = 0; i < videoList.size(); i++) {
            for (j = 0; j < users.size(); j++) {
                for (k = 0; k < users.get(j).getFavourite().size(); k++) {
                    if (videoList.get(i).getName().equals(users.get(j).getFavourite().get(k))) {
                        videoList.get(i).incFavs();
                        break;
                    }
                }
            }
        }

        for (i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getFavs() == 0) {
                videoList.remove(i);
                i--;
            }
        }

        if (sortType.equals("asc")) {
            videoList.sort(comparator::ascVideoFavs);
        } else {
            videoList.sort(comparator::descVideoFavs);
        }

        if (videoList.size() < number) {
            for (i = 0; i < videoList.size() - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(videoList.size() - 1).getName());
        } else {
            for (i = 0; i < number - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(number - 1).getName());
        }

        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     * Se filtreaza la fel ca la functiile precedente obiectele de tip object in functie de an si
     * gen numai ca in lista finala se adauga si duratele videoclipurilor. Se sorteaza lista in
     * functie de durata si nume in ordinea data de sortType si se adauga in rezultat lista finala
     * luand in considerare numarul dat ca parametru sau de marimea listei. In final se returneaza
     * structura de tip Result.
     *
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param years Anul dupa care se filtreaza videoclipurile;
     * @param genres Genurile dupa care se filtreaza videoclipurile;
     * @param sortType Tipul de sortare;
     * @param object Tipul de obiect care se filtreaza (Filme / Seriale);
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result longest(final ArrayList<Movie> movies, final ArrayList<Show> shows,
                          final List<String> years, final List<String> genres,
                          final String sortType, final String object, final int number,
                          final int actionId) {
        result.setId(actionId);

        int nrFilters;
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;
        String yearHelper;

        if (object.equals("movies")) {
            if (movies.size() == 0) {
                result.setMessage("Query result: []");
                return result;
            }

            for (i = 0; i < movies.size(); i++) {

                nrFilters = 0;
                yearHelper = "" + movies.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < movies.get(i).getGenres().size(); k++) {
                            if (movies.get(i).getGenres().get(k).equals(genres.get(j))) {
                                nrFilters++;
                                break;
                            }

                            if (genres.get(j) == null) {
                                nrFilters = 1;
                            }
                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(movies.get(i).getName());
                    video.setLength(movies.get(i).getDuration());
                    videoList.add(video);
                }
            }
        } else {
            if (shows.size() == 0) {
                result.setMessage("Query result: []");
                return result;
            }

            for (i = 0; i < shows.size(); i++) {

                nrFilters = 0;
                yearHelper = "" + shows.get(i).getYear();

                if (yearHelper.equals(years.get(0)) || years.get(0) == null) {
                    for (j = 0; j < genres.size(); j++) {
                        for (k = 0; k < shows.get(i).getGenres().size(); k++) {
                            if (shows.get(i).getGenres().get(k).equals(genres.get(j))) {
                                nrFilters++;
                                break;
                            }

                            if (genres.get(j) == null) {
                                nrFilters = 1;
                            }

                        }
                    }
                }

                if (nrFilters == genres.size()) {
                    video = new VideoActionList();
                    video.setName(shows.get(i).getName());
                    video.setLength(shows.get(i).getwholeDuration());
                    videoList.add(video);
                }
            }
        }

        if (videoList.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        if (sortType.equals("asc")) {
            videoList.sort(comparator::ascLength);
        } else {
            videoList.sort(comparator::descLength);
        }

        if (videoList.size() < number) {
            for (i = 0; i < videoList.size() - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(videoList.size() - 1).getName());
        } else {
            for (i = 0; i < number - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(number - 1).getName());
        }

        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     * Se filtreaza lista de videoclipuri de tip object folosind functia createArrayList, se adauga
     * pentru fiecare titlu din lista numarul de vizualizari iterand prin lista de utilizatori, se
     * elimina videoclipurile care nu au fost deloc vazute si se adauga in rezultat lista finala
     * (a carei marime depinde de number si de marimea listei sortate). In final se retuneaza
     * structura de tip Result.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param years Anul dupa care se filtreaza videoclipurile;
     * @param genres Genurile dupa care se filtreaza videoclipurile;
     * @param sortType Tipul de sortare;
     * @param object Tipul de obiect care se filtreaza (Filme / Seriale);
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result mostViewed(final ArrayList<User> users, final ArrayList<Movie> movies,
                              final ArrayList<Show> shows, final List<String> years,
                              final List<String> genres, final String sortType,
                              final String object, final int number, final int actionId) {
        result.setId(actionId);

        ArrayList<VideoActionList> videoList;

        videoList = createActionList(movies, shows, years, genres, object);

        for (i = 0; i < videoList.size(); i++) {
            for (j = 0; j < users.size(); j++) {
                if (users.get(j).getHistory().containsKey(videoList.get(i).getName())) {
                    videoList.get(i).addViews(users.get(j).getHistory().get(
                            videoList.get(i).getName()));
                }
            }
        }

        if (sortType.equals("asc")) {
            videoList.sort(comparator::ascViews);
        } else {
            videoList.sort(comparator::descViews);
        }

        if (videoList.size() == 1) {
            if (videoList.get(0).getViews() != 0) {
                result.setMessage("Query result: [" + videoList.get(0).getName() + "]");
                return result;
            }
        }

        for (i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getViews() == 0) {
                videoList.remove(i);
            }
        }

        if (videoList.size() == 0) {
            result.setMessage("Query result: []");
            return result;
        }

        if (videoList.size() < number) {
            for (i = 0; i < videoList.size() - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(videoList.size() - 1).getName());

        } else {
            for (i = 0; i < number - 1; i++) {
                helper.append(videoList.get(i).getName());
                helper.append(", ");
            }

            helper.append(videoList.get(number - 1).getName());

        }

        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }

    /**
     * Functia itereaza prin fiecare utilizator si verifica daca numarul de rating-uri dat este
     * mai mare decat 0. In caz pozitiv, se adauga intr-o lista. Se sorteaza lista in functie de
     * nota de evaluare si nume in ordinea sortType si se adauga in rezultat un top al celor mai
     * activi utilizatori. In final se returneaza rezultatul.
     *
     * @param users Baza de date a utilizatorilor;
     * @param sortType Tipul de sortare;
     * @param number Numarul maxim de instante de afisat;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result topRating(final ArrayList<User> users, final String sortType, final int number,
                             final int actionId) {

        result.setId(actionId);
        ArrayList<User> userList = new ArrayList<>();

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getNumberRatings() > 0) {
                userList.add(users.get(i));
            }
        }

        if (sortType.equals("asc")) {
            userList.sort(comparator::ascUser);
        } else {
            userList.sort(comparator::descUser);
        }

        if (userList.size() == 0) {
            result.setMessage("Query result :[]");
            return result;
        }

        if (userList.size() < number) {
            for (i = 0; i < userList.size() - 1; i++) {
                helper.append(userList.get(i).getUsername());
                helper.append(", ");
            }

            helper.append(userList.get(userList.size() - 1).getUsername());
        } else {
            for (i = 0; i < number - 1; i++) {
                helper.append(userList.get(i).getUsername());
                helper.append(", ");
            }

            helper.append(userList.get(number - 1).getUsername());
        }

        result.setMessage("Query result: [" + helper.toString() + "]");
        return result;
    }
}
