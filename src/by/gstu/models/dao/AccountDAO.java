package by.gstu.models.dao;

import by.gstu.models.entities.Account;

import java.util.Collection;

public interface AccountDAO {
    boolean create(Account account);
    Collection<Account> readAll();
    Account read(int id);
    boolean update(Account account);
    boolean delete(int id);

    Account logIn(String login, String password);
    boolean availableLogin(String login);
}
