package by.gstu.models.dao;

import by.gstu.models.entities.Order;

import java.util.Calendar;
import java.util.Collection;

/**
 * @author Evgeniy Trofimov
 * @version 2.1
 */
public interface OrderDAO {
    boolean create(Order order);
    Collection<Order> readAll();
    Order read(int id);
    boolean update(Order order);
    boolean delete(int id);

    Collection<Order> readAll(int clientId);
    boolean closeOrder(int orderId, Calendar returnDate);
}
