package user;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.List;

public final class UserList {
    private ArrayList<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public UserList(final List<UserInputData> usersData) {
        this();
        int i;

        for (i = 0; i < usersData.size(); i++) {
            User user =
                    new User(usersData.get(i).getUsername(), usersData.get(i).getSubscriptionType(),
                            usersData.get(i).getHistory(), usersData.get(i).getFavoriteMovies());
            users.add(user);
        }

    }
}
