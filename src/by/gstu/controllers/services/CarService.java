package by.gstu.controllers.services;

import by.gstu.models.dao.CarDAO;
import by.gstu.models.dao.DAOFactory;
import by.gstu.models.entities.Car;
import by.gstu.models.entities.Entity;
import by.gstu.models.untils.ParserJSON;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public class CarService {

    private static CarService instance;
    private final CarDAO carDAO;

    private CarService() {
        carDAO = DAOFactory.getDAOFactory().getCarDAO();
    }

    public static CarService getInstance() {
        if (instance == null) {
            instance = new CarService();
        }
        return instance;
    }

    public JSONArray getAllCars() {
        var cars = carDAO.readAll();
        Collection<Entity> entities = new ArrayList<>(cars);
        return ParserJSON.toJSONArray(entities);
    }
}
