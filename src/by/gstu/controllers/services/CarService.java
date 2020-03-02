package by.gstu.controllers.services;

import by.gstu.models.dao.CarDAO;
import by.gstu.models.dao.DAOFactory;
import by.gstu.models.utils.ParserJSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
        return ParserJSON.toJSONArray(new ArrayList<>(carDAO.readAll()));
    }

    public JSONObject getCar(int id) {
        return carDAO.read(id).toJSON();
    }
}
