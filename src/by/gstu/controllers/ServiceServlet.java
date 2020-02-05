package by.gstu.controllers;

import by.gstu.controllers.services.CarService;
import by.gstu.controllers.services.OrderService;
import by.gstu.controllers.services.UserService;
import by.gstu.models.dao.CarDAO;
import by.gstu.models.entities.Client;
import by.gstu.models.entities.Order;
import by.gstu.models.untils.ConfigurationManager;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handel request
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
@WebServlet(urlPatterns = "/service/*")
public class ServiceServlet extends HttpServlet {
    private CarService carService;
    private OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (servletPath.contains("/cars")) {

            carService = CarService.getInstance();

            if (servletPath.matches("/cars/\\d+")) {
                String[] partUrl = servletPath.split("/");
                int id = Integer.parseInt(partUrl[partUrl.length - 1]);
                resp.getWriter().write(carService.getCar(id).toString());
            } else {
                resp.getWriter().write(carService.getAllCars().toString());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (servletPath.contains("/orders")) {
            if (UserService.checkAccess(req.getSession()) == UserService.AccessUser.CLIENT) {
                orderService = OrderService.getInstance();
                resp.getWriter().write(orderService.createOrder(OrderService.getOrderReq(req)).toString());
            }
        }
    }
}

