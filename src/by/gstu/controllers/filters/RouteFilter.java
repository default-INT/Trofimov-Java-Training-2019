package by.gstu.controllers.filters;

import by.gstu.controllers.services.PageService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "RouteFilter", urlPatterns = "/*")
public class RouteFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (PageService.routeRequest(request, response))
            chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {
    }


}
