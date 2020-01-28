package by.gstu.models.entities;

import org.json.JSONObject;

/**
 * Entity class.
 * Extends Account.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class Administrator extends Account {
    public Administrator(int id, String login, String password, String email, String fullName) {
        super(id, login, password, email, fullName);
    }

    public Administrator() {
    }
}
