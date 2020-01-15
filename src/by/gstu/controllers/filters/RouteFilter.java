package by.gstu.controllers.filters;

import by.gstu.controllers.services.PageService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@WebFilter(filterName = "RouteFilter", urlPatterns = "/*")
public class RouteFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        PageService.routeRequest(request, response);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    private Cookie searchCookie(String key, HttpServletRequest request) {
        
        return null;
    }

}
