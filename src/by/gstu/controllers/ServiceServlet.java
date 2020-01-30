package by.gstu.controllers;

import by.gstu.controllers.services.CarService;
import by.gstu.models.dao.CarDAO;
import by.gstu.models.untils.ConfigurationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (servletPath.contains("/cars")) {

            carService = CarService.getInstance();

            resp.getWriter().write(carService.getAllCars().toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
