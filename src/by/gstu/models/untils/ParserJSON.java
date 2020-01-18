package by.gstu.models.untils;

import by.gstu.models.entities.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Description
 *
 * @author Evgeniy Trofimov
 * @version 1.0
 */
public interface ParserJSON {
    /**
     * <h1>Convert entity to Json object.<h1/>
     * @return JSON car
     */
    JSONObject toJSON();

    /**
     * <h1>Convert collection entities to json object</h1>
     * @param entities
     * @return
     */
    static JSONArray toJSONArray(Collection<Entity> entities) {
        JSONArray jsonEntities = new JSONArray();
        for (Entity entity : entities)
            jsonEntities.put(entity.toJSON());
        return  jsonEntities;
    }
}
