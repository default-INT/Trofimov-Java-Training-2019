package by.gstu.controllers.services;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.dao.OrderDAO;
import by.gstu.models.entities.Car;
import by.gstu.models.entities.Entity;
import by.gstu.models.entities.Order;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OrderService {

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

    public static Order getOrderReq(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
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
            int carId = jsonObject.getInt("carId");
            int rentalPeriod = jsonObject.getInt("rentalPeriod");

            Car car = DAOFactory.getDAOFactory().getCarDAO().read(carId);
            double price = rentalPeriod * car.getPriceHour();
            int clientId = ((Entity) request.getSession().getAttribute("authAccount")).getId();

            return new Order(
                    orderDateCal,
                    rentalPeriod,
                    carId,
                    clientId,
                    jsonObject.getString("passportData"),
                    price
            );
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parse(String input) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" );

        if (input.endsWith("Z")) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }
        return df.parse(input);
    }
}