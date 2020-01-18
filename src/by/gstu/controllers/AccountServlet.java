package by.gstu.controllers;

import by.gstu.controllers.models.User;
import by.gstu.controllers.services.UserService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AccountServlet", urlPatterns = "/account")
public class AccountServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        //request.getRea
        User user = userService.registration(getUserReq(request));
        if (user != null) {
            response.getWriter().write(user.toJSON().toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = userService.authorization(login, password);
        if (user != null) {
            response.getWriter().write(user.toJSON().toString());
        }
    }

    private static User getUserReq(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject jsonObject =  new JSONObject(buffer.toString());
            return new User(jsonObject.getString("login"), jsonObject.getString("email"), jsonObject.getString("password"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new User();
    }
}
