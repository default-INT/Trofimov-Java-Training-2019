package by.gstu.controllers.services;

import by.gstu.models.untils.ConfigurationManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * The class routes the client request and returns the corresponding response.
 *
 * @author Evgeniy Trofimov
 * @version 1.3
 */
public class PageService {

    private static final Logger logger = Logger.getLogger(PageService.class);

    private final static String LAYOUT_PATH = "/layout.html";
    private final static String NOT_FOUND_PATH = "/notfound.html";
    private final static String MAIN_PATH = "/pages/main_content.html";
    private final static String CAR_LIST_PATH = "/pages/car_list.html";
    private final static String ABOUT_PATH = "/pages/about.html";
    private final static String FREE_CAR_PATH = "/pages/free_car.html";
    private final static String RENTAL_CAR_PATH = "/pages/cars.html";
    private final static String USER_CAR_PATH = "/pages/user_car.html";

    private final static String MAIN_PAGE = "/main";
    private final static String CAR_LIST_PAGE = "/car-list";
    private final static String ABOUT_PAGE = "/about";
    private final static String FREE_CAR_PAGE = "/free-car";
    private final static String RENTAL_CAR_PAGE = "/cars";
    private final static String USER_CAR_PAGE = "/user-car";
    private final static String NOT_FOUND_PAGE = "/not-found";

    private static Map<String, String> pagePath;

    private final static Collection<String> RESOURCES_PAGE;

    static {
        RESOURCES_PAGE = Arrays.asList("/resources", "/route", "/favicon.ico", "/account", "/service");
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();

        try {
            pagePath = configurationManager.getUrlsPath("urls");
        } catch (Exception ex) {
            pagePath = new HashMap<>();
            pagePath.put(MAIN_PAGE, MAIN_PATH);
            pagePath.put(NOT_FOUND_PAGE, NOT_FOUND_PATH);
            pagePath.put(CAR_LIST_PAGE, CAR_LIST_PATH);
            pagePath.put(ABOUT_PAGE, ABOUT_PATH);
            pagePath.put(FREE_CAR_PAGE, FREE_CAR_PATH);
            pagePath.put(RENTAL_CAR_PAGE, RENTAL_CAR_PATH);
            pagePath.put(USER_CAR_PAGE, USER_CAR_PATH);
        }
    }

    private PageService() {
    }

    public static boolean routeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        for (String page : RESOURCES_PAGE) {
            if (servletPath.contains(page)) return true;
        }

        Collection<String> accessPages = pagePath.keySet();

        for (String page : accessPages) {
            if (servletPath.equals(page)) {
                request.getRequestDispatcher(LAYOUT_PATH).forward(request, response);
                return true;
            }
        }
        if (servletPath.equals(NOT_FOUND_PAGE)) {
            request.getRequestDispatcher(NOT_FOUND_PATH).forward(request, response);
            return false;
        } else if (servletPath.equals("/")) {
            response.sendRedirect(MAIN_PAGE);
            return false;
        } else {
            response.sendRedirect(NOT_FOUND_PAGE);
            return false;
        }
    }

    public static void servletRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String pathInfo = request.getPathInfo();
        ArrayList<HashMap.Entry> pageInPath = new ArrayList<>(pagePath.entrySet());

        for (HashMap.Entry element : pageInPath) {
            if (pathInfo.contains(element.getKey().toString())) {
                request.getRequestDispatcher(element.getValue().toString()).forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + NOT_FOUND_PAGE);
    }
}
