package by.gstu.models.dao;

import by.gstu.models.entities.Client;

import java.util.Collection;

public interface ClientDAO {
    boolean create(Client client);
    Collection<Client> readAll();
    boolean update(Client client);
}
