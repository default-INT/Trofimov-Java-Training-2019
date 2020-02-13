package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.entities.Account;
import by.gstu.models.entities.Administrator;
import by.gstu.models.entities.Client;

import javax.servlet.http.HttpSession;

/**
 *
 * @author  Evgeniy Trofimov
 * @version 0.1
 */
public class UserService {

    public enum AccessUser {
        ADMIN, CLIENT, GUEST
    }

    private static UserService instance;
    private DAOFactory dao;

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

    public static AccessUser checkAccess(HttpSession session) {
        Account logInAccount = (Account) session.getAttribute("authAccount");
        if (logInAccount == null) {
            return AccessUser.GUEST;
        } else if (logInAccount instanceof Client) {
            return AccessUser.CLIENT;
        } else if (logInAccount instanceof Administrator) {
            return AccessUser.ADMIN;
        }
        return AccessUser.GUEST;
    }
}
