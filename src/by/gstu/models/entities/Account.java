package by.gstu.models.entities;

import org.json.JSONObject;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public abstract class Account extends Entity {
    protected String login;
    protected String password;
    protected String email;
    protected String fullName;

    public Account(int id, String login, String password, String email, String fullName) {
        super(id);
        this.login = login;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public Account() {
    }

    @Override
    public JSONObject toJSON() {
        JSONObject accountJson = new JSONObject();

        accountJson.put("id", id);
        accountJson.put("login", login);
        accountJson.put("email", email);
        accountJson.put("fullName", fullName);

        return accountJson;
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
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
