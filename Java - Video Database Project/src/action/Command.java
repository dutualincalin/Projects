package action;

import entertainment.Movie;
import entertainment.Show;
import user.User;
import utils.Result;

import java.util.ArrayList;

/**
 * Aceasta clasa este folosita pentru executarea tuturor comenzilor.
 * @author Dutu Alin Calin
 */
public final class Command {
    /**
     * Aceasta functie are rol de a adauga la lista de favorite un video unui anumit utilizator.
     * In primul rand se verifica daca utilizatorul exista in baza de date. Daca exista, atunci se
     * cauta in instoric daca exista videoclipul in cauza. Daca nu exista se returneaza mesajul
     * corespunzator, dar daca exista atunci se verifica daca video-ul exista in lista de favorite.
     * In caz pozitiv se returneaza mesajul corespunzator, altfel se adauga clipul in lista si se
     * returneaza mesajul de finalizare a operatiunii. Id-ul actiunii va fi si el retunat impreuna
     * cu mesajul ca rezultat.
     * @param username Numele utilizatorului;
     * @param title Titlul videoclipului;
     * @param users Baza de date a utilizatorilor;
     * @param actionId Id-ul actiunii;
     * @return Structura de tip rezultat care contine id-ul si mesajul actiunii.
     */
    public Result favorite(final String username, final String title, final ArrayList<User> users,
                           final int actionId) {
        int i;
        int j;
        Result result = new Result();

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getHistory().get(title) == null) {
                    result.setId(0);
                    result.setMessage("error -> " + title + " is not seen");
                    return result;
                }

                for (j = 0; j < users.get(i).getFavourite().size(); j++) {

                    if (users.get(i).getFavourite().get(j).equals(title)) {
                        result.setId(0);
                        result.setMessage("error -> " + title + " is already in favourite list");
                        return result;
                    }
                }

                users.get(i).getFavourite().add(title);
                result.setId(actionId);
                result.setMessage("success -> " + title + " was added as favourite");
                return result;
            }

        }
        return result;
    }

    /**
     * Aceasta functie are rol de a adauga un video cu o vizualizare in istoricul unui utilizator
     * daca videoclipul nu exista in istoric sau incrementeaza numarul de vizualizari daca
     * videoclipul exista in istoric. La final se returneaza mesajul corespunzator impreuna cu id-ul
     * actiunii sub forma de rezultat.
     * @param username Numele utilizatorului;
     * @param title Titlul video-ului;
     * @param users Baza de date a utilizatorilor;
     * @param actionId Id-ul actiunii;
     * @return Structura de tip rezultat care contine id-ul si mesajul actiunii.
     */
    public Result view(final String username, final String title, final ArrayList<User> users,
                       final int actionId) {
        int i;
        Result result = new Result();

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                if (users.get(i).getHistory().get(title) == null) {
                    users.get(i).getHistory().put(title, 1);
                    result.setId(actionId);
                    result.setMessage("success -> " + title + " was viewed with total views of 1");
                    break;
                }

                if (users.get(i).getHistory().get(title) >= 1) {
                    users.get(i).getHistory()
                            .replace(title, users.get(i).getHistory().get(title) + 1);
                    result.setId(actionId);
                    result.setMessage("success -> " + title + " was viewed with total views of "
                            + users.get(i).getHistory().get(title));
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Functia cauta user-ul corespunzator apoi cauta filmul sau serial-ul corespunzator si daca
     * utilizatorul a evaluat deja filmul se trimite mesajul corespunzator. Daca nu, atunci se
     * adauga nota in lista, se incrementeaza numarul de utilizatori care au evaluat videoclipul si
     * numarul de evaluari ale utilizatorului in cauza si se adauga mesajul corespunzator in
     * rezultat. In final se returneaza rezultatul.
     * @param username Numele utilizatorului;
     * @param title Titlul video-ului;
     * @param grade Rating-ul oferit;
     * @param season Numarul sezonului;
     * @param users Baza de date a utilizatorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @param actionId Id-ul actiunii;
     * @return Structura de tip rezultat care contine id-ul si mesajul actiunii.
     */
    public Result rating(final String username, final String title, final double grade,
                         final int season, final ArrayList<User> users,
                         final ArrayList<Movie> movies, final ArrayList<Show> shows,
                         final int actionId) {
        int i;
        int j;
        Result result = new Result();

        for (i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {

                if (users.get(i).getHistory().get(title) == null) {
                    result.setId(0);
                    result.setMessage("error -> " + title + " is not seen");
                    return result;
                }

                for (j = 0; j < movies.size(); j++) {
                    if (movies.get(j).getName().equals(title)) {
                        if (movies.get(j).checkUserRated(username)) {
                            result.setId(actionId);
                            result.setMessage("error -> " + title + " has been already rated");
                        } else {
                            movies.get(j).addRating(grade);
                            movies.get(j).addUserRated(username);
                            result.setId(actionId);
                            result.setMessage(
                                    "success -> " + title + " was rated with " + grade + " by "
                                            + username);
                            users.get(i).addRating();
                        }
                        return result;
                    }
                }

                for (j = 0; j < shows.size(); j++) {
                    if (shows.get(j).getName().equals(title)) {
                        if (shows.get(j).getSeasons().get(season - 1).checkUserRated(username)) {
                            result.setId(actionId);
                            result.setMessage("error -> " + title + " has been already rated");
                        } else {
                            shows.get(j).getSeasons().get(season - 1).getRatings().add(grade);
                            shows.get(j).getSeasons().get(season - 1).addUserRated(username);
                            result.setId(actionId);
                            result.setMessage(
                                    "success -> " + title + " was rated with " + grade + " by "
                                            + username);
                            users.get(i).addRating();
                        }

                        return result;
                    }
                }
            }
        }

        return result;
    }
}
