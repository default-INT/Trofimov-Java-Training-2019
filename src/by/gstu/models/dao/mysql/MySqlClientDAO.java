package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ClientDAO;
import by.gstu.models.entities.Client;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements ClientDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
class MySqlClientDAO implements ClientDAO {

    private static final Logger logger = Logger.getLogger(MySqlClientDAO.class);

    private static final String DEFAULT_CREATE = "CALL add_client(?, ?, ?, ?, ?)";
    private static final String DEFAULT_READ_ALL = "CALL read_all_clients()";
    private static final String DEFAULT_UPDATE = "CALL edit_client(?, ?, ?, ?, ?)";

    private static final String CREATE;
    private static final String READ_ALL;
    private static final String UPDATE;

    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();

        CREATE = configurateManager.getProperty("sql.Clients.create", DEFAULT_CREATE, "mysql");
        READ_ALL = configurateManager.getProperty("sql.Clients.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configurateManager.getProperty("sql.Clients.update", DEFAULT_UPDATE, "mysql");
    }

    @Override
    public boolean create(Client client) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(CREATE);

            pStatement.setString(1, client.getLogin());
            pStatement.setString(2, client.getPassword());
            pStatement.setString(3, client.getEmail());
            pStatement.setString(4, client.getFullName());
            pStatement.setInt(5, client.getBirthdayYear());

            int k = pStatement.executeUpdate();
            if (k > 0) return true;
            return false;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Collection<Client> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String email = resultSet.getString("email");
                String fullName = resultSet.getString("full_name");
                int birthdayYear = resultSet.getInt("birthday_year");

                clients.add(new Client(id, login, email, fullName, birthdayYear));
            }
            return clients;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Client client) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(UPDATE);

            pStatement.setInt(1, client.getId());
            pStatement.setString(2, client.getLogin());
            pStatement.setString(3, client.getPassword());
            pStatement.setString(4, client.getEmail());
            pStatement.setString(5, client.getFullName());
            pStatement.setInt(6, client.getBirthdayYear());

            int k = pStatement.executeUpdate();
            return k > 0 ? true : false;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }
}
