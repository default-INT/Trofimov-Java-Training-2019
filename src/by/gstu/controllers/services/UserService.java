package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.entities.Account;
import by.gstu.models.entities.Client;
import by.gstu.models.entities.User;

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
    private DAOFactory dao;
    private Collection<User> users;

    private UserService() {
        dao = DAOFactory.getDAOFactory();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public Account authorization(String login, String password) {

        return dao.getAccountDAO().logIn(login, password);
    }

    public Account authorization(Account account) {
        return authorization(account.getLogin(), account.getPassword());
    }

    public Client registration(String login, String email, String password) {
        return registration(login, email, password);
    }

    public Client registration(Client client) {
        Client newClient = null;
        if (dao.getClientDAO().create(client)) newClient = (Client) authorization(client);
        return newClient;
    }
}
