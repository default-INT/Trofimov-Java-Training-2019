package by.gstu.models.dao;

import by.gstu.models.dao.hibernate.HibDAOFactory;
import by.gstu.models.dao.mysql.MySqlDAOFactory;
import by.gstu.models.entities.car.Car;
import by.gstu.models.entities.car.FuelType;
import by.gstu.models.entities.car.Transmission;
import by.gstu.models.utils.ConfigurationManager;
import org.apache.log4j.Logger;

/**
 * DAO factory TODO
 *
 * @author Evgeniy Trofimov
 * @version 2.2
 */
public abstract class DAOFactory {

    private static final Logger logger = Logger.getLogger(DAOFactory.class);

    private enum DataBase{
        MYSQL, HIBERNATE
    }

    public abstract AccountDAO getAccountDAO();
    public abstract AdministratorDAO getAdministratorDAO();
    public abstract CarDAO getCarDAO();
    public abstract ClientDAO getClientDAO();
    public abstract OrderDAO getOrderDAO();
    public abstract ReturnRequestDAO getReturnRequestDAO();
    public abstract CarDAO.CarEntityDAO<FuelType> getFuelTypeDAO();
    public abstract CarDAO.CarEntityDAO<Transmission> getTransmissionDAO();

    public static DAOFactory getDAOFactory() {
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        return getDAOFactory(configurationManager.getProperty("db.orm"));
    }

    public static DAOFactory getDAOFactory(String dataBase) {
        try{
            DataBase db = DataBase.valueOf(dataBase.toUpperCase());
            switch (db) {
                case MYSQL: return new MySqlDAOFactory();
                case HIBERNATE: return new HibDAOFactory();
                default: return null;
            }
        }catch (IllegalArgumentException ex) {
            logger.fatal("Uncknow database. " + ex.getMessage());
            return null;
        }
    }
}
