package by.gstu.models.dao.hibernate;

import by.gstu.models.dao.*;
import by.gstu.models.entities.car.FuelType;
import by.gstu.models.entities.car.Transmission;

/**
 * @version 1.0
 * @author Evgeniy Trofimov
 */
public class HibDAOFactory extends DAOFactory {
    @Override
    public AccountDAO getAccountDAO() {
        return new HibernateAccountDAO();
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        return new HibernateAdministratorDAO();
    }

    @Override
    public CarDAO getCarDAO() {
        return new HibernateCarDAO();
    }

    @Override
    public ClientDAO getClientDAO() {
        return new HibernateClientDAO();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return new HibernateOrderDAO();
    }

    @Override
    public ReturnRequestDAO getReturnRequestDAO() {
        return new HibernateReturnRequestDAO();
    }

    @Override
    public CarDAO.CarEntityDAO<FuelType> getFuelTypeDAO() {
        return new HibernateCarDAO.HibernateFuelTypeDAO();
    }

    @Override
    public CarDAO.CarEntityDAO<Transmission> getTransmissionDAO() {
        return new HibernateCarDAO.HibernateTransmissionDAO();
    }
}
