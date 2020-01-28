package by.gstu.models.dao.mysql;

import by.gstu.models.dao.*;
import by.gstu.models.entities.Car;

public class MySqlDAOFactory extends DAOFactory {

    @Override
    public AccountDAO getAccountDAO() {
        return new MySqlAccountDAO();
    }

    @Override
    public AdministratorDAO getAdministratorDAO() {
        return new MySqlAdministratorDAO();
    }

    @Override
    public CarDAO getCarDAO() {
        return new MySqlCarDAO();
    }

    @Override
    public ClientDAO getClientDAO() {
        return new MySqlClientDAO();
    }

    @Override
    public OrderDAO getOrderDAO() {
        return new MySqlOrderDAO();
    }

    @Override
    public ReturnRequestDAO getReturnRequestDAO() {
        return new MySqlReturnRequestDAO();
    }

    @Override
    public CarDAO.CarEntityDAO<Car.FuelType> getFuelTypeDAO() {
        return new MySqlCarDAO.MySqlFuelTypeDAO();
    }

    @Override
    public CarDAO.CarEntityDAO<Car.Transmission> getTransmissionDAO() {
        return new MySqlCarDAO.MySqlTransmissionDAO();
    }
}
