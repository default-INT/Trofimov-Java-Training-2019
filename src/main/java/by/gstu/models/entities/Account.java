package by.gstu.models.entities;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
@javax.persistence.Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role")
@NamedQuery(
        name="logIn",
        query="SELECT a.id, a.login, a.email from Account a " +
                "WHERE a.login = :login AND a.password = :password")
public abstract class Account extends Entity {
    @Column(unique = true)
    protected String login;
    protected String password;
    @Column(unique = true)
    protected String email;
    @Column(name = "full_name")
    protected String fullName;

    public Account(int id, String login, String password, String email, String fullName) {
        super(id);
        this.login = login;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public Account(String login, String password, String email, String fullName) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    public Account(int id, String login, String email, String fullName) {
        super(id);
        this.login = login;
        this.email = email;
        this.fullName = fullName;
    }

    public Account() {
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

    @Override
    public JSONObject toJSON() {
        JSONObject accountJson = super.toJSON();

        accountJson.put("login", login);
        accountJson.put("email", email);
        accountJson.put("fullName", fullName);

        return accountJson;
    }
}
