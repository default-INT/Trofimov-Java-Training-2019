package by.gstu.controllers;

import by.gstu.controllers.services.PageService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/route/*")
public class RouteServlet extends javax.servlet.http.HttpServlet {
    String mainPath = "/pages/main_content.html";
    String carListPath = "/pages/car_list.html";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageService.servletRoute(request, response);
    }
}
