package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ClientDAO;
import by.gstu.models.entities.Client;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements ClientDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.1
 */
class MySqlClientDAO implements ClientDAO {

    private static final Logger logger = Logger.getLogger(MySqlClientDAO.class);

    private static final String DEFAULT_CREATE = "{CALL add_client(?, ?, ?, ?, ?)}";
    private static final String DEFAULT_READ_ALL = "{CALL read_all_clients()}";
    private static final String DEFAULT_UPDATE = "{CALL edit_client(?, ?, ?, ?, ?)}";

    private static final String CREATE;
    private static final String READ_ALL;
    private static final String UPDATE;

    static {
        ConfigurationManager configuratorManager = ConfigurationManager.getInstance();

        CREATE = configuratorManager.getProperty("sql.Clients.create", DEFAULT_CREATE, "mysql");
        READ_ALL = configuratorManager.getProperty("sql.Clients.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configuratorManager.getProperty("sql.Clients.update", DEFAULT_UPDATE, "mysql");
    }

    @Override
    public boolean create(Client client) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(CREATE);

            callStatement.setString("var_login", client.getLogin());
            callStatement.setString("var_password", client.getPassword());
            callStatement.setString("var_email", client.getEmail());
            callStatement.setString("var_full_name", client.getFullName());
            callStatement.setInt("var_birthday_year", client.getBirthdayYear());

            int k = callStatement.executeUpdate();
            return k > 0;
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
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ_ALL);
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
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(UPDATE);

            callStatement.setInt("var_id", client.getId());
            callStatement.setString("var_login", client.getLogin());
            callStatement.setString("var_password", client.getPassword());
            callStatement.setString("var_email", client.getEmail());
            callStatement.setString("var_full_name", client.getFullName());
            callStatement.setInt("var_birthday_year", client.getBirthdayYear());

            int k = callStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }
}
