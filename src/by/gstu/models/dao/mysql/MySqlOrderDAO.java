package by.gstu.models.dao.mysql;

import by.gstu.models.dao.OrderDAO;
import by.gstu.models.entities.Order;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Implements OrderDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class MySqlOrderDAO implements OrderDAO {

    private static final Logger logger = Logger.getLogger(MySqlOrderDAO.class);

    private static final String DEFAULT_CREATE = "CALL add_order(?, ?, ?, ?, ?, ?)";
    private static final String DEFAULT_READ = "CALL read_order(?)";
    private static final String DEFAULT_READ_ALL = "CALL read_all_orders()";
    private static final String DEFAULT_UPDATE = "CALL edit_order(?, ?, ?, ?, ?, ?, ?)";
    private static final String DEFAULT_DELETE = "CALL delete_order(?)";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;

    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();

        CREATE = configurateManager.getProperty("sql.Clients.create", DEFAULT_CREATE, "mysql");
        READ = configurateManager.getProperty("sql.Clients.read", DEFAULT_READ, "mysql");
        READ_ALL = configurateManager.getProperty("sql.Clients.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configurateManager.getProperty("sql.Clients.update", DEFAULT_UPDATE, "mysql");
        DELETE = configurateManager.getProperty("sql.Clients.delete", DEFAULT_DELETE, "mysql");
    }

    @Override
    public boolean create(Order order) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(CREATE);

            pStatement.setDate(1, new Date(order.getOrderDate().getTimeInMillis()),
                    order.getOrderDate());
            pStatement.setInt(2, order.getPeriod());
            pStatement.setInt(3, order.getClientId());
            pStatement.setInt(4, order.getCarId());
            pStatement.setString(5, order.getPassportData());
            pStatement.setDouble(6, order.getPrice());

            int k = pStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Collection<Order> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date orderDate = resultSet.getDate("order_date");
                int rentalPeriod = resultSet.getInt("rental_period");
                Date returnDate = resultSet.getDate("return_date");
                int clientId = resultSet.getInt("client_id");
                int carId = resultSet.getInt("car_id");
                String passportData = resultSet.getString("passport_data");
                double price = resultSet.getDouble("price");

                Calendar orderDateCal = new GregorianCalendar();
                orderDateCal.setTime(orderDate);

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                orders.add(new Order(id, orderDateCal, rentalPeriod, returnDateCal,
                        clientId, carId, passportData, price));
            }
            return orders;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public Order read(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Date orderDate = resultSet.getDate("order_date");
                int rentalPeriod = resultSet.getInt("rental_period");
                Date returnDate = resultSet.getDate("return_date");
                int clientId = resultSet.getInt("client_id");
                int carId = resultSet.getInt("car_id");
                String passportData = resultSet.getString("passport_data");
                double price = resultSet.getDouble("price");

                Calendar orderDateCal = new GregorianCalendar();
                orderDateCal.setTime(orderDate);

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                return new Order(id, orderDateCal, rentalPeriod, returnDateCal,
                        clientId, carId, passportData, price);
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Order order) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(UPDATE);

            pStatement.setInt(1, order.getId());
            pStatement.setDate(2, new Date(order.getOrderDate().getTimeInMillis()),
                    order.getOrderDate());
            pStatement.setInt(3, order.getPeriod());
            pStatement.setInt(4, order.getClientId());
            pStatement.setInt(5, order.getCarId());
            pStatement.setString(6, order.getPassportData());
            pStatement.setDouble(7, order.getPrice());

            int k = pStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(DELETE);

            pStatement.setInt(1, id);

            int k = pStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }
}
