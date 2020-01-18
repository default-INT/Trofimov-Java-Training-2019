package by.gstu.controllers.services;

import by.gstu.controllers.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author  Evgeniy Trofimov
 * @version 0.1
 */
public class UserService {
    private static UserService instance;
    private Collection<User> users;

    private UserService() {
        users = new ArrayList<>();

        users.add(new User("admin", "admin@gmail.com", "a1806"));
        users.add(new User("default", "default@gmail.com", "a1806"));
        users.add(new User("Trofimov", "evgen.trofimov.2000@gmail.com", "a1806"));
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public User authorization(String login, String password) {
        final User user = users.stream().filter(u -> u.getLogin().equals(login) && u.getPassword().equals(password))
                                        .findFirst()
                                        .get();
        return user;
    }

    public User authorization(User user) {
        return authorization(user.getLogin(), user.getPassword());
    }

    public User registration(String login, String email, String password) {
        return registration(new User(login, email, password));
    }

    public User registration(User user) {
        Optional<User> findUser = users.stream()
                                   .filter(u -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail()))
                                   .findFirst();
        if (findUser.isEmpty()) {
            try {
                users.add(user);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return new User(user.getLogin(), user.getEmail());
        } else
            return null;
    }
}
