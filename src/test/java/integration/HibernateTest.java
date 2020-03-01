package integration;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.entities.car.Car;

import java.util.Collection;

public class HibernateTest {
    public static void main(String[] args) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory();
        Collection<Car> cars = daoFactory.getCarDAO().readAll();
        //Collection<Client> clients = daoFactory.getClientDAO().readAll();
        cars.forEach(System.out::println);
    }
}
