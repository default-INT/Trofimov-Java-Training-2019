package by.gstu.models.dao.mysql;

import by.gstu.models.dao.AdministratorDAO;
import by.gstu.models.entities.Administrator;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implements AdministratorDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
class MySqlAdministratorDAO implements AdministratorDAO {

    private static final Logger logger = Logger.getLogger(MySqlAdministratorDAO.class);

    private static final String DEFAULT_CREATE = "CALL add_administrator(?, ?, ?, ?)";

    private static final String CREATE;

    static {
        ConfigurationManager configuratorManager = ConfigurationManager.getInstance();
        CREATE = configuratorManager.getProperty("sql.Administrators.create", DEFAULT_CREATE, "mysql");
    }

    @Override
    public boolean create(Administrator administrator) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(CREATE);

            callStatement.setString("var_login", administrator.getLogin());
            callStatement.setString("var_password", administrator.getPassword());
            callStatement.setString("var_email", administrator.getEmail());
            callStatement.setString("var_full_name", administrator.getFullName());
            callStatement.setBoolean(4, true);

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
