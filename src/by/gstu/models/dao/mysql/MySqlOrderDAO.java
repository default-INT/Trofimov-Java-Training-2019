package by.gstu.models.dao.mysql;

import by.gstu.models.dao.OrderDAO;
import by.gstu.models.entities.Order;

import java.util.Collection;

/**
 * Implements OrderDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class MySqlOrderDAO implements OrderDAO {
    @Override
    public boolean create(Order order) {
        return false;
    }

    @Override
    public Collection<Order> readAll() {
        return null;
    }

    @Override
    public Order read(int id) {
        return null;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
