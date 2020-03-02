package by.gstu.models.dao.mysql;

import by.gstu.models.dao.AccountDAO;
import by.gstu.models.entities.Account;
import by.gstu.models.entities.Administrator;
import by.gstu.models.entities.Client;
import by.gstu.models.utils.ConfigurationManager;
import by.gstu.models.utils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements AccountDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.1
 */
class MySqlAccountDAO implements AccountDAO {

    private static final Logger logger = Logger.getLogger(MySqlAccountDAO.class);

    private static final String DEFAULT_CREATE = "{CALL add_account(?, ?, ?, ?, ?, ?)}";
    private static final String DEFAULT_READ = "{CALL read_account(?)}";
    private static final String DEFAULT_READ_ALL = "{CALL read_all_accounts()}";
    private static final String DEFAULT_UPDATE = "{CALL edit_account(?, ?, ?, ?)}";
    private static final String DEFAULT_DELETE = "{CALL delete_account(?)}";

    private static final String DEFAULT_LOG_IN = "{CALL log_in(?, ?)}";
    private static final String DEFAULT_CHECK_LOGIN = "{CALL check_login(?)}";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;
    private static final String LOG_IN;
    private static final String CHECK_LOGIN;

    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();

        CREATE = configurateManager.getProperty("sql.Accounts.create", DEFAULT_CREATE, "mysql");
        READ = configurateManager.getProperty("sql.Accounts.read", DEFAULT_READ, "mysql");
        READ_ALL = configurateManager.getProperty("sql.Accounts.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configurateManager.getProperty("sql.Accounts.update", DEFAULT_UPDATE, "mysql");
        DELETE = configurateManager.getProperty("sql.Accounts.delete", DEFAULT_DELETE, "mysql");
        LOG_IN = configurateManager.getProperty("sql.Accounts.logIn", DEFAULT_LOG_IN, "mysql");
        CHECK_LOGIN = configurateManager.getProperty("sql.Accounts.checkLogin", DEFAULT_CHECK_LOGIN,
                "mysql");
    }

    @Override
    public boolean create(Account account) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(CREATE);

            callStatement.setString("var_login", account.getLogin());
            callStatement.setString("var_password", account.getPassword());
            callStatement.setString("var_email", account.getEmail());
            callStatement.setString("var_full_name", account.getFullName());
            if (account.getClass().equals(Client.class)) {
                callStatement.setBoolean(4, false);
                callStatement.setInt(6, ((Client) account).getBirthdayYear());
            } else if (account.getClass().equals(Administrator.class)) {
                callStatement.setBoolean(4, true);
            }

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
    public Collection<Account> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String email = resultSet.getString("email");
                String fullName = resultSet.getString("full_name");
                boolean role = resultSet.getBoolean("role");

                if (role) {
                    accounts.add(new Administrator(id, login, email, fullName));
                } else {
                    int birthdayYear = resultSet.getInt("birthday_year");
                    accounts.add(new Client(id, login, email, fullName, birthdayYear));
                }
            }
            return accounts;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public Account read(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ);
            statement.setInt("var_id", id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String fullName = resultSet.getString("full_name");
                boolean role = resultSet.getBoolean("role");

                if (role) {
                    return new Administrator(id, login, password, email, fullName);
                } else {
                    int birthdayYear = resultSet.getInt("birthday_year");
                    return  new Client(id, login, password, email, fullName, birthdayYear);
                }
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Account account) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(UPDATE);

            callStatement.setInt("var_id", account.getId());
            callStatement.setString("var_login", account.getLogin());
            callStatement.setString("var_password", account.getPassword());
            callStatement.setString("var_email", account.getEmail());
            callStatement.setString("var_full_name", account.getFullName());

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
    public boolean delete(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(DELETE);

            callStatement.setInt("var_id", id);

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
    public Account logIn(String login, String password) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(LOG_IN);
            statement.setString("var_login", login);
            statement.setString("var_password", password);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String fullName = resultSet.getString("full_name");
                boolean role = resultSet.getBoolean("role");
                if (role) {
                    return new Administrator(id, login, password, email, fullName);
                } else {
                    int birthdayYear = resultSet.getInt("birthday_year");
                    return  new Client(id, login, password, email, fullName, birthdayYear);
                }
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean availableLogin(String login) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(CHECK_LOGIN);
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int num = resultSet.getInt(1);
                return num <= 0;
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }
}
