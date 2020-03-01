package by.gstu.models.entities;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Entity class.
 * Extends Account.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("true")
public class Administrator extends Account {
    public Administrator(int id, String login, String password, String email, String fullName) {
        super(id, login, password, email, fullName);
    }

    public Administrator() {
    }

    public Administrator(int id, String login, String email, String fullName) {
        super(id, login, email, fullName);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject adminJson = super.toJSON();
        adminJson.put("status", "admin");
        return adminJson;
    }
}
