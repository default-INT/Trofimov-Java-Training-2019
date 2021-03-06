package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.dao.OrderDAO;
import by.gstu.models.entities.Car;
import by.gstu.models.entities.Entity;
import by.gstu.models.entities.Order;
import by.gstu.models.utils.ParserJSON;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class);

    private static OrderService instance;
    private OrderDAO orderDAO;

    private OrderService() {
        orderDAO = DAOFactory.getDAOFactory().getOrderDAO();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public JSONObject createOrder(Order order) {
        JSONObject msg = new JSONObject();
        if (orderDAO.create(order)) {
            msg.put("result", true);
        } else {
            msg.put("result", false);
        }
        return msg;
    }

    public JSONArray clientOrders(int clientId) {
        Collection<Order> orders = orderDAO.readAll(clientId);
        if (orders == null) return null;
        return ParserJSON.toJSONArray(orders);
    }

    public JSONObject closeOrder(int orderId, Date returnDate) {
        JSONObject result = new JSONObject();
        Calendar returnDateCal = new GregorianCalendar();
        returnDateCal.setTime(returnDate);
        result.put("result", orderDAO.closeOrder(orderId, returnDateCal));
        return result;
    }

    public static Order getOrderReq(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject jsonObject =  new JSONObject(buffer.toString());

            Date orderDate = parse(jsonObject.getString("orderDate"));
            Calendar orderDateCal = new GregorianCalendar();
            orderDateCal.setTime(orderDate);

            Date returnDate = parse(jsonObject.getString("returnDate"));
            Calendar returnDateCal = new GregorianCalendar();
            returnDateCal.setTime(returnDate);

            int carId = jsonObject.getInt("carId");
            int rentalPeriod = (int) ((returnDateCal.getTimeInMillis()
                    - orderDateCal.getTimeInMillis()) / 1000 / 3600);

            Car car = DAOFactory.getDAOFactory().getCarDAO().read(carId);
            double price = rentalPeriod * car.getPriceHour();
            int clientId = ((Entity) request.getSession().getAttribute("authAccount")).getId();

            return new Order(
                    orderDateCal,
                    returnDateCal,
                    rentalPeriod,
                    carId,
                    clientId,
                    jsonObject.getString("passportData"),
                    price
            );
        } catch (IOException | ParseException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static Date parse(String input) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        if (input.endsWith("Z")) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset);

            input = s0 + "GMT" + s1;
        }
        return df.parse(input);
    }
}
