package by.gstu.models.dao;

import by.gstu.models.entities.Order;

import java.util.Collection;

public interface OrderDAO {
    boolean create(Order order);
    Collection<Order> readAll();
    Order read(int id);
    boolean update(Order order);
    boolean delete(int id);
}
