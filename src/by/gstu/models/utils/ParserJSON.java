package by.gstu.models.utils;

import by.gstu.models.entities.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Description
 *
 * @author Evgeniy Trofimov
 * @version 1.1
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

    /**
     * <h1>Parse calendar to json format.</h1>
     *
     * @param calendar
     * @return JSON date
     */
    static String parseCalendar(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" + addZero(calendar.get(Calendar.MONTH) + 1) + "-" +
                addZero(calendar.get(Calendar.DATE)) + "T" + addZero(calendar.getTime().getHours()) +
                ":" + addZero(calendar.get(Calendar.MINUTE));
    }

    private static String addZero(int num) {
        return num < 10 ? "0" + num : Integer.toString(num);
    }
}
