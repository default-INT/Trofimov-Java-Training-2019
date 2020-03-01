package by.gstu.models.entities.car;

import by.gstu.models.entities.Entity;
import org.json.JSONObject;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class CarEntity extends Entity {
    protected String name;

    public CarEntity(int id, String name) {
        super(id);
        this.name = name;
    }

    public CarEntity(String name) {
        this.name = name;
    }

    public CarEntity() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject carJson = new JSONObject();

        carJson.put("name", getName());

        return carJson;
    }
}
