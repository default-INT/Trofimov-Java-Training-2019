package by.gstu.controllers;

import by.gstu.models.entities.Account;
import by.gstu.models.entities.Client;
import by.gstu.controllers.services.UserService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AccountServlet", urlPatterns = "/account/*")
public class AccountServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Client user = userService.registration(getUserReq(request));
        if (user != null) {
            response.getWriter().write(user.toJSON().toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getPathInfo();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (servletPath.contains("/auth")) {
            Account logInAccount;
            HttpSession session = request.getSession();

            String login = request.getParameter("login");
            String password = request.getParameter("password");

            if (login == null && password == null) {

                logInAccount = (Account) session.getAttribute("authAccount");
            } else {
                logInAccount = userService.authorization(login, password);
                session.setAttribute("authAccount", logInAccount);
            }
            if (logInAccount != null) {
                response.getWriter().write(logInAccount.toJSON().toString());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getPathInfo();
        HttpSession session = req.getSession();
        if (servletPath.contains("/exit")) {
            session.setAttribute("authAccount", null);
        }
    }

    private static Client getUserReq(HttpServletRequest request) {
        StringBuffer buffer = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject jsonObject =  new JSONObject(buffer.toString());
            return new Client(jsonObject.getString("login"), jsonObject.getString("password"),
                    jsonObject.getString("email"), jsonObject.getString("fullName"),
                    jsonObject.getInt("birthdayYear"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
