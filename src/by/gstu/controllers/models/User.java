package by.gstu.controllers.models;

import org.json.JSONObject;

public class User {
    private String login;
    private String email;
    private String password;

    public User(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <h1>Convert car to Json object.<h1/>
     * @return JSON car
     */
    public JSONObject toJSON() {
        JSONObject userJson = new JSONObject();

        userJson.put("login", login);
        userJson.put("email", email);

        return userJson;
    }

    @Override
    public int hashCode() {
        return login.hashCode() + email.hashCode();
    }
}
