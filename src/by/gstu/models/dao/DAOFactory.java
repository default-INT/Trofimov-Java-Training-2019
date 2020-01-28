package by.gstu.models.dao;

import by.gstu.models.dao.mysql.MySqlDAOFactory;
import by.gstu.models.entities.Car;
import org.apache.log4j.Logger;

/**
 * DAO factory TODO
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public abstract class DAOFactory {

    private static final Logger logger = Logger.getLogger(DAOFactory.class);

    private enum DataBase{
        MYSQL
    }

    public abstract AccountDAO getAccountDAO();
    public abstract AdministratorDAO getAdministratorDAO();
    public abstract CarDAO getCarDAO();
    public abstract ClientDAO getClientDAO();
    public abstract OrderDAO getOrderDAO();
    public abstract ReturnRequestDAO getReturnRequestDAO();
    public abstract CarDAO.CarEntityDAO<Car.FuelType> getFuelTypeDAO();
    public abstract CarDAO.CarEntityDAO<Car.Transmission> getTransmissionDAO();

    public static DAOFactory getDAOFactory() {
        return new MySqlDAOFactory();
    }

    public static DAOFactory getDAOFactory(String dataBase) {
        try{
            DataBase db = DataBase.valueOf(dataBase.toUpperCase());
            switch (db) {
                case MYSQL: return new MySqlDAOFactory();
                default: return null;
            }
        }catch (IllegalArgumentException ex) {
            logger.fatal("Uncknow database. " + ex.getMessage());
            return null;
        }
    }
}
