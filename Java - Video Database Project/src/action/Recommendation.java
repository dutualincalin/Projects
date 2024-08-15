package action;

import entertainment.Genre;
import entertainment.Movie;
import entertainment.Show;
import entertainment.VideoActionList;
import user.User;
import utils.Result;

import java.util.ArrayList;

/**
 * Aceasta clasa este folosita pentru executarea recomandarilor.
 */
public final class Recommendation {
    private final Comparators comparator = new Comparators();

    /**
     * Functia verifica daca genul este unul valid(Daca genul se afla in enum-ul Genres).
     * @param genre Genul unui videoclip;
     * @return returneaza adevarat daca genul este valid sau fals in caz contrar.
     */
    public boolean checkGenre(final String genre) {

        for (Genre g : Genre.values()) {
            if (g.name().equals(genre.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Functia itereaza in lista tuturor videoclipurilor si verifica fiecare clip daca exista in
     * istoricul utilizatorului. Primul clip pe care il gaseste nevizualizat il adauga in rezultat.
     * Daca nu il gaseste atunci adauga un mesaj de esec. In final se returneaza rezultatul.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param username Numele utilizatorului;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result standard(final ArrayList<User> users, final ArrayList<Movie> movies,
                           final ArrayList<Show> shows, final String username,
                           final int actionId) {
        Result result = new Result();
        result.setId(actionId);
        int i, j;

        for (i = 0; i < users.size(); i++) {

            if (users.get(i).getUsername().equals(username)) {
                for (j = 0; j < movies.size(); j++) {
                    if (!(users.get(i).getHistory().containsKey(movies.get(j).getName()))) {
                        result.setMessage(
                                "StandardRecommendation result: " + movies.get(j).getName());
                        return result;
                    }
                }

                for (j = 0; j < shows.size(); j++) {
                    if (!(users.get(i).getHistory().containsKey(shows.get(j).getName()))) {
                        result.setMessage(
                                "StandardRecommendation result: " + shows.get(j).getName());
                        return result;
                    }
                }
            }
        }

        result.setMessage("StandardRecommendation cannot be applied!");
        return result;
    }

    /**
     * Functia cauta utilizatorul in baza de date si daca il gaseste atunci itereaza in toata lista
     * de videoclipuri si adauga intr-o alta lista acele clipuri nevizualizat care au nota de
     * evaluare mai mare decat 0. Urmeaza sortarea listei filtrate in mod descrescator in functie
     * de nota de evaluare. Dac lista finala nu este goala, atunci se adauga in rezultat primul
     * videoclip din lista. Altfel se itereaza din nou in toata lista de videoclipuri si se alege
     * acel videoclip care nu exista in istoricul utilizatorului username. In final se returneaza
     * rezultatul.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param username Numele utilizatorului;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result bestUnseen(final ArrayList<User> users, final ArrayList<Movie> movies,
                              final ArrayList<Show> shows, final String username,
                              final int actionId) {
        Result result = new Result();
        result.setId(actionId);
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;

        int i, userInd = -1;

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                userInd = i;
                break;
            }
        }

        if (userInd == -1) {
            result.setMessage("BestRatedUnseenRecommendation cannot be applied!");
            return result;
        }

        if (movies.size() == 0 && shows.size() == 0) {
            result.setMessage("BestRatedUnseenRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < movies.size(); i++) {
            if (movies.get(i).getRating() > 0 && (!(users.get(userInd).getHistory().
                    containsKey(movies.get(i).getName())))) {
                video = new VideoActionList();
                video.setRating(movies.get(i).getRating());
                video.setName(movies.get(i).getName());
                videoList.add(video);
            }
        }

        for (i = 0; i < shows.size(); i++) {
            if (shows.get(i).getRating() > 0 && (!(users.get(userInd).getHistory().
                    containsKey(shows.get(i).getName())))) {
                video = new VideoActionList();
                video.setRating(shows.get(i).getRating());
                video.setName(shows.get(i).getName());
                videoList.add(video);
            }
        }

        videoList.sort(comparator::bestRating);

        if (videoList.size() != 0) {
            result.setMessage("BestRatedUnseenRecommendation result: "
                    + videoList.get(0).getName());
            return result;
        } else {
            for (i = 0; i < movies.size(); i++) {
                if (!(users.get(userInd).getHistory().containsKey(movies.get(i).getName()))) {
                    result.setMessage("BestRatedUnseenRecommendation result: "
                            + movies.get(i).getName());
                    return result;
                }
            }

            for (i = 0; i < shows.size(); i++) {
                if (!(users.get(userInd).getHistory().containsKey(shows.get(i).getName()))) {
                    result.setMessage("BestRatedUnseenRecommendation result: "
                            + shows.get(i).getName());
                    return result;
                }
            }
        }

        result.setMessage("BestRatedUnseenRecommendation cannot be applied!");
        return result;
    }

    /**
     * Functia cauta utilizatorul in baza de date si daca il gaseste, il verifica daca are abonament
     * Premium. Se itereaza prin toate videoclipurile si se face o lista cu toate genurile si
     * numarul total de vizualizari ale acestora. Se elimina din lista genurile invalide verificand
     * cu functia checkGenre si apoi lista se va sorta descrescator in functie de numarul de
     * vizualizari. Se itereaza lista de genuri, se itereaza si lista de videoclipuri si se adauga
     * in rezultat primul video care se potriveste. In final se returneaza rezultatul.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param username Numele utilizatorului;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result popularVideo(final ArrayList<User> users, final ArrayList<Movie> movies,
                                final ArrayList<Show> shows, final String username,
                                final int actionId) {

        Result result = new Result();
        result.setId(actionId);
        ArrayList<VideoActionList> genreList = new ArrayList<>();
        VideoActionList genre;
        int views;

        int i, j, k, contains, genreInd = -1, userInd = -1;

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getSubscription().equals("BASIC")) {
                    result.setMessage("PopularRecommendation cannot be applied!");
                    return result;
                } else {
                    userInd = i;
                    break;
                }
            }
        }

        if (userInd == -1) {
            result.setMessage("PopularRecommendation cannot be applied!");
            return result;
        }

        if (movies.size() == 0 && shows.size() == 0) {
            result.setMessage("PopularRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < shows.size(); i++) {
            views = 0;

            for (j = 0; j < users.size(); j++) {
                if (users.get(j).getHistory().get(shows.get(i).getName()) != null) {
                    views += users.get(j).getHistory().get(shows.get(i).getName());
                }
            }

            for (j = 0; j < shows.get(i).getGenres().size(); j++) {
                contains = 0;

                for (k = 0; k < genreList.size(); k++) {
                    if (genreList.get(k).getName().equals(shows.get(i).getGenres().get(j))) {
                        contains = 1;
                        genreInd = k;
                        break;
                    }
                }

                if (contains == 1) {
                    genreList.get(genreInd).addViews(views);
                } else {
                    genre = new VideoActionList();
                    genre.setName(shows.get(i).getGenres().get(j));
                    genre.addViews(views);
                    genreList.add(genre);
                }
            }
        }

        for (i = 0; i < movies.size(); i++) {
            views = 0;

            for (j = 0; j < users.size(); j++) {
                if (users.get(j).getHistory().get(movies.get(i).getName()) != null) {
                    views += users.get(j).getHistory().get(movies.get(i).getName());
                }
            }

            for (j = 0; j < movies.get(i).getGenres().size(); j++) {
                contains = 0;

                for (k = 0; k < genreList.size(); k++) {
                    if (genreList.get(k).getName().equals(movies.get(i).getGenres().get(j))) {
                        contains = 1;
                        genreInd = k;
                        break;
                    }
                }

                if (contains == 1) {
                    genreList.get(genreInd).addViews(views);
                } else {
                    genre = new VideoActionList();
                    genre.setName(movies.get(i).getGenres().get(j));
                    genre.addViews(views);
                    genreList.add(genre);
                }
            }
        }


        for (i = 0; i < genreList.size(); i++) {
            if (!checkGenre(genreList.get(i).getName())) {
                genreList.remove(i);
                i--;
            }
        }

        if (genreList.size() == 0) {
            result.setMessage("PopularRecommendation cannot be applied!");
            return result;
        }

        genreList.sort(comparator::mostPopular);

        for (i = 0; i < genreList.size(); i++) {
            for (j = 0; j < movies.size(); j++) {
                for (k = 0; k < movies.get(j).getGenres().size(); k++) {
                    if (movies.get(j).getGenres().get(k).equals(genreList.get(i).getName())) {
                        if (!users.get(userInd).getHistory()
                                .containsKey(movies.get(j).getName())) {
                            result.setMessage(
                                    "PopularRecommendation result: " + movies.get(j).getName());
                            return result;
                        }
                    }
                }
            }

            for (j = 0; j < shows.size(); j++) {
                for (k = 0; k < shows.get(j).getGenres().size(); k++) {
                    if (shows.get(j).getGenres().get(k).equals(genreList.get(i).getName())) {
                        if (!users.get(userInd).getHistory().containsKey(shows.get(j).getName())) {
                            result.setMessage(
                                    "PopularRecommendation result: " + shows.get(j).getName());
                            return result;
                        }
                    }
                }
            }
        }

        result.setMessage("PopularRecommendation cannot be applied!");
        return result;
    }

    /**
     * Functia ia toate videoclipurile si le adauga intr-o lista, apoi iterand prin listele de
     * videoclipuri favorite ale tuturor utilizatorilor se adauga si numarul de aparitii ale
     * tuturor videoclipurilor in aceste liste. Se sorteaza lista descrescator in functie de numarul
     * de aparitii si se adauga la rezultat primul videoclip din lista finala care nu se afla in
     * istoricul utilizatorului cu numele dat de parametrul username.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param username Numele utilizatorului;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result favoriteVideo(final ArrayList<User> users, final ArrayList<Movie> movies,
                                 final ArrayList<Show> shows, final String username,
                                 final int actionId) {

        Result result = new Result();
        result.setId(actionId);
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;
        int i, j, k, userInd = -1;

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getSubscription().equals("BASIC")) {
                    result.setMessage("FavoriteRecommendation cannot be applied!");
                    return result;
                }
            }
        }

        if (movies.size() == 0 && shows.size() == 0) {
            result.setMessage("FavoriteRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getSubscription().equals("BASIC")) {
                    result.setMessage("FavoriteRecommendation cannot be applied!");
                    return result;
                }

                userInd = i;
                break;
            }
        }

        if (userInd == -1) {
            result.setMessage("FavoriteRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < movies.size(); i++) {
            video = new VideoActionList();
            video.setName(movies.get(i).getName());
            videoList.add(video);
        }

        for (i = 0; i < shows.size(); i++) {
            video = new VideoActionList();
            video.setName(shows.get(i).getName());
            videoList.add(video);
        }

        for (i = 0; i < users.size(); i++) {
            for (j = 0; j < users.get(i).getFavourite().size(); j++) {
                for (k = 0; k < videoList.size(); k++) {
                    if (users.get(i).getFavourite().get(j).equals(videoList.get(k).getName())) {
                        videoList.get(k).incFavs();
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

        videoList.sort(comparator::descFavs);

        for (i = 0; i < videoList.size(); i++) {
            for (j = 0; j < users.get(userInd).getFavourite().size(); j++) {
                if (!users.get(userInd).getHistory().containsKey(videoList.get(i).getName())) {
                    result.setMessage(
                            "FavoriteRecommendation result: " + videoList.get(i).getName());
                    return result;
                }
            }
        }

        result.setMessage("FavoriteRecommendation cannot be applied!");
        return result;
    }

    /**
     * Functia ia toate videoclipurile si le adauga intr-o lista numai daca au genul genre, apoi
     * face o sortare crescatoare in functie de rating si nume, elimina videoclipurile care se afla
     * in istoricul utilizatorului username si adauga in rezultat lista prelucrata. in final se
     * returneaza rezultatul.
     *
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param genre Genul cerut;
     * @param username Numele utilizatorului;
     * @param actionId Id-ul actiunii;
     * @return O structura de tip rezultat care va returna id-ul si mesajul final.
     */
    public Result search(final ArrayList<User> users, final ArrayList<Movie> movies,
                         final ArrayList<Show> shows, final String genre, final String username,
                         final int actionId) {

        Result result = new Result();
        result.setId(actionId);
        ArrayList<VideoActionList> videoList = new ArrayList<>();
        VideoActionList video;
        StringBuilder helper = new StringBuilder();
        int i, j, userInd = -1;

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getSubscription().equals("BASIC")) {
                    result.setMessage("SearchRecommendation cannot be applied!");
                    return result;
                }

                userInd = i;
                break;
            }
        }

        if (userInd == -1) {
            result.setMessage("SearchRecommendation cannot be applied!");
            return result;
        }

        if (movies.size() == 0 && shows.size() == 0) {
            result.setMessage("SearchRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < movies.size(); i++) {
            for (j = 0; j < movies.get(i).getGenres().size(); j++) {
                if (movies.get(i).getGenres().get(j).equals(genre)) {
                    video = new VideoActionList();
                    video.setName(movies.get(i).getName());
                    video.setRating(movies.get(i).getRating());
                    videoList.add(video);
                    break;
                }
            }
        }

        for (i = 0; i < shows.size(); i++) {
            for (j = 0; j < shows.get(i).getGenres().size(); j++) {
                if (shows.get(i).getGenres().get(j).equals(genre)) {
                    video = new VideoActionList();
                    video.setName(shows.get(i).getName());
                    video.setRating(shows.get(i).getRating());
                    videoList.add(video);
                    break;
                }
            }
        }

        videoList.sort(comparator::ascVideoRating);

        for (i = 0; i < videoList.size(); i++) {
            if (users.get(userInd).getHistory().containsKey(videoList.get(i).getName())) {
                videoList.remove(i);
                i--;
            }
        }

        if (videoList.size() == 0) {
            result.setMessage("SearchRecommendation cannot be applied!");
            return result;
        }

        for (i = 0; i < videoList.size() - 1; i++) {
            helper.append(videoList.get(i).getName());
            helper.append(", ");
        }

        helper.append(videoList.get(videoList.size() - 1).getName());
        result.setMessage("SearchRecommendation result: [" + helper.toString() + "]");
        return result;
    }
}
