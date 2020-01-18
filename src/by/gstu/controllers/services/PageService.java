package by.gstu.controllers.services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * The class routes the client request and returns the corresponding response.
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
public class PageService {
    private final static String LAYOUT_PATH = "/layout.html";
    private final static String INDEX_PATH = "/index.html";
    private final static String NOT_FOUND_PATH = "/notfound.html";
    private final static String MAIN_PATH = "/pages/main_content.html";
    private final static String CAR_LIST_PATH = "/pages/car_list.html";
    private final static String ABOUT_PATH = "/pages/about.html";
    private final static String FREE_CAR_PATH = "/pages/free_car.html";
    private final static String RENTAL_CAR_PATH = "/pages/rental_car.html";
    private final static String USER_CAR_PATH = "/pages/user_car.html";

    private final static String MAIN_PAGE = "/main";
    private final static String CAR_LIST_PAGE = "/car-list";
    private final static String ABOUT_PAGE = "/about";
    private final static String FREE_CAR_PAGE = "/free-car";
    private final static String RENTAL_CAR_PAGE = "/rental-car";
    private final static String USER_CAR_PAGE = "/user-car";
    private final static String NOT_FOUND_PAGE = "/not-found";

    private final static Collection<String> PAGES;
    private final static Map<String, String> PAGE_PATH;

    //add read form file
    static {
        PAGES = Arrays.asList(MAIN_PAGE, CAR_LIST_PAGE, ABOUT_PAGE, FREE_CAR_PAGE, RENTAL_CAR_PAGE, USER_CAR_PAGE);

        PAGE_PATH = new HashMap<>();

        PAGE_PATH.put(MAIN_PAGE, MAIN_PATH);
        PAGE_PATH.put(NOT_FOUND_PAGE, NOT_FOUND_PATH);
        PAGE_PATH.put(CAR_LIST_PAGE, CAR_LIST_PATH);
        PAGE_PATH.put(ABOUT_PAGE, ABOUT_PATH);
        PAGE_PATH.put(FREE_CAR_PAGE, FREE_CAR_PATH);
        PAGE_PATH.put(RENTAL_CAR_PAGE, RENTAL_CAR_PATH);
        PAGE_PATH.put(USER_CAR_PAGE, USER_CAR_PATH);
    }

    private PageService() {
    }

    public static boolean routeRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (!servletPath.contains("/resources") && !servletPath.contains("/route") && !servletPath.contains("/favicon.ico")
            && !servletPath.contains("/account")) {
            for (String page : PAGES) {
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
        return true;
    }

    public static void servletRoute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        ArrayList<HashMap.Entry> pageInPath = new ArrayList<>(PAGE_PATH.entrySet());

        for (HashMap.Entry element : pageInPath) {
            if (pathInfo.contains(element.getKey().toString())) {
                request.getRequestDispatcher(element.getValue().toString()).forward(request, response);
                return;
            }
        }
        response.sendRedirect(request.getContextPath() + NOT_FOUND_PAGE);
    }
}
