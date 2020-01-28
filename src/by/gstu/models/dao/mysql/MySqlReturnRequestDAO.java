package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.entities.ReturnRequest;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Implements ReturnRequestDAO.
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public class MySqlReturnRequestDAO implements ReturnRequestDAO {

    private static final Logger logger = Logger.getLogger(MySqlOrderDAO.class);

    private static final String DEFAULT_CREATE = "CALL add_return_request(?, ?, ?, ?)";
    private static final String DEFAULT_READ = "CALL read_return_request(?)";
    private static final String DEFAULT_READ_ALL = "CALL read_all_return_requests()";
    private static final String DEFAULT_UPDATE = "CALL edit_return_request(?, ?, ?, ?, ?)";
    private static final String DEFAULT_DELETE = "CALL delete_return_request(?)";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;

    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();

        CREATE = configurateManager.getProperty("sql.ReturnRequests.create", DEFAULT_CREATE, "mysql");
        READ = configurateManager.getProperty("sql.ReturnRequests.read", DEFAULT_READ, "mysql");
        READ_ALL = configurateManager.getProperty("sql.ReturnRequests.readAll", DEFAULT_READ_ALL,
                "mysql");
        UPDATE = configurateManager.getProperty("sql.ReturnRequests.update", DEFAULT_UPDATE, "mysql");
        DELETE = configurateManager.getProperty("sql.ReturnRequests.delete", DEFAULT_DELETE, "mysql");
    }

    @Override
    public boolean create(ReturnRequest returnRequest) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(CREATE);

            pStatement.setDate(1, new Date(returnRequest.getReturnDate().getTimeInMillis()),
                    returnRequest.getReturnDate());
            pStatement.setInt(2, returnRequest.getOrderId());
            pStatement.setString(3, returnRequest.getDescription());
            pStatement.setBoolean(4, returnRequest.isReturnMark());
            pStatement.setDouble(5, returnRequest.getRepairCost());

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
    public Collection<ReturnRequest> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<ReturnRequest> returnRequests = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date returnDate = resultSet.getDate("return_date");
                int orderId = resultSet.getInt("order_id");
                String description = resultSet.getString("description");
                boolean returnMark = resultSet.getBoolean("return_mark");
                double repairCost = resultSet.getDouble("repair_cost");

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                returnRequests.add(new ReturnRequest(id, returnDateCal, orderId, description, returnMark, repairCost));
            }
            return returnRequests;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public ReturnRequest read(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Date returnDate = resultSet.getDate("return_date");
                int orderId = resultSet.getInt("order_id");
                String description = resultSet.getString("description");
                boolean returnMark = resultSet.getBoolean("return_mark");
                double repairCost = resultSet.getDouble("repair_cost");

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                return new ReturnRequest(id, returnDateCal, orderId, description, returnMark, repairCost);
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(ReturnRequest returnRequest) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(UPDATE);

            pStatement.setInt(1, returnRequest.getId());
            pStatement.setDate(2, new Date(returnRequest.getReturnDate().getTimeInMillis()),
                    returnRequest.getReturnDate());
            pStatement.setInt(3, returnRequest.getOrderId());
            pStatement.setString(4, returnRequest.getDescription());
            pStatement.setBoolean(5, returnRequest.isReturnMark());
            pStatement.setDouble(6, returnRequest.getRepairCost());

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
        PreparedStatement pStatement;
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
