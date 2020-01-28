package by.gstu.models.dao.mysql;

import by.gstu.models.dao.AdministratorDAO;
import by.gstu.models.entities.Administrator;
import by.gstu.models.untils.ConfigurationManager;
import org.apache.log4j.Logger;

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
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();
        CREATE = configurateManager.getProperty("sql.Administrators.create", DEFAULT_CREATE, "mysql");
    }

    @Override
    public boolean create(Administrator administrator) {
        return false;
    }
}
