package action;

import actor.Actor;
import entertainment.Movie;
import entertainment.Show;
import fileio.ActionInputData;
import user.User;
import utils.Result;

import java.util.ArrayList;


/**
 * Aceasta clasa este folosita pentru executarea tuturor actiunilor.
 * @author Dutu Alin Calin
 */
public final class Action {
    /**
     * Aceasta functie verifica mai intai categoria actiunii si apoi tipul actiunii. In functie de
     * tipul actiunii se ruleaza functia corespunzatoare. Fiecare functie apelata are ca output o
     * structura de tip Result pe care aceasta functie o va returna si ea Main-ului.
     * Variabilele year, genre, words si awards sunt pentru a extrage filtrele.
     * @param action Baza de date a actiunii;
     * @param users Baza de date a utilizatorilor;
     * @param actors Baza de date a actorilor;
     * @param movies Baza de date a filmelor;
     * @param shows Baza de date a serialelor;
     * @return Structura de tip rezultat care contine id-ul si mesajul actiunii rulate.
     */
    public Result execute(final ActionInputData action, final ArrayList<User> users,
                          final ArrayList<Actor> actors, final ArrayList<Movie> movies,
                          final ArrayList<Show> shows) {

        final int year = 0;
        final int genre = 1;
        final int words = 2;
        final int awards = 3;

        Result result = new Result();

        if (action.getActionType().equals("command")) {
            Command command = new Command();

            if (action.getType().equals("favorite")) {
                return (command.favorite(action.getUsername(), action.getTitle(), users,
                        action.getActionId()));
            }

            if (action.getType().equals("view")) {
                return (command.view(action.getUsername(), action.getTitle(), users,
                        action.getActionId()));
            }

            if (action.getType().equals("rating")) {
                return (command.rating(action.getUsername(), action.getTitle(), action.getGrade(),
                        action.getSeasonNumber(), users, movies, shows, action.getActionId()));
            }
        }

        if (action.getActionType().equals("query")) {
            Query query = new Query();

            if (action.getCriteria().equals("average")) {
                return (query.average(movies, shows, action.getSortType(), action.getNumber(),
                        action.getActionId()));
            }

            if (action.getCriteria().equals("awards")) {
                return (query.awards(actors, action.getFilters().get(awards), action.getSortType(),
                        action.getActionId()));
            }

            if (action.getCriteria().equals("filter_description")) {

                return (query.description(actors, action.getFilters().get(words),
                        action.getSortType(), action.getActionId()));
            }

            if (action.getCriteria().equals("ratings")) {
                return (query.ratings(movies, shows, action.getFilters().get(year),
                        action.getFilters().get(genre), action.getSortType(),
                        action.getObjectType(), action.getNumber(), action.getActionId()));
            }

            if (action.getCriteria().equals("favorite")) {
                return (query.favorite(users, movies, shows, action.getFilters().get(year),
                        action.getFilters().get(genre), action.getSortType(),
                        action.getObjectType(), action.getNumber(), action.getActionId()));
            }

            if (action.getCriteria().equals("longest")) {
                return (query.longest(movies, shows, action.getFilters().get(year),
                        action.getFilters().get(genre), action.getSortType(),
                        action.getObjectType(), action.getNumber(), action.getActionId()));
            }

            if (action.getCriteria().equals("most_viewed")) {
                return (query.mostViewed(users, movies, shows, action.getFilters().get(year),
                        action.getFilters().get(genre), action.getSortType(),
                        action.getObjectType(), action.getNumber(), action.getActionId()));
            }

            if (action.getCriteria().equals("num_ratings")) {
                return (query.topRating(users, action.getSortType(), action.getNumber(),
                        action.getActionId()));
            }

        }

        if (action.getActionType().equals("recommendation")) {
            Recommendation recommendation = new Recommendation();

            if (action.getType().equals("standard")) {
                return (recommendation.standard(users, movies, shows, action.getUsername(),
                        action.getActionId()));
            }

            if (action.getType().equals("best_unseen")) {
                return (recommendation.bestUnseen(users, movies, shows, action.getUsername(),
                        action.getActionId()));
            }

            if (action.getType().equals("popular")) {
                return (recommendation.popularVideo(users, movies, shows, action.getUsername(),
                        action.getActionId()));
            }

            if (action.getType().equals("favorite")) {
                return (recommendation.favoriteVideo(users, movies, shows, action.getUsername(),
                        action.getActionId()));
            }

            if (action.getType().equals("search")) {
                return (recommendation.search(users, movies, shows, action.getGenre(),
                        action.getUsername(), action.getActionId()));
            }
        }

        return result;
    }
}
