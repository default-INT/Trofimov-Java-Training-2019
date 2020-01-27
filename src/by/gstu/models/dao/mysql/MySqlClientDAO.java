package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ClientDAO;
import by.gstu.models.entities.Client;

import java.util.Collection;

/**
 * Implements ClientDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class MySqlClientDAO implements ClientDAO {
    @Override
    public boolean create(Client client) {
        return false;
    }

    @Override
    public Collection<Client> readAll() {
        return null;
    }

    @Override
    public boolean update(Client client) {
        return false;
    }
}
