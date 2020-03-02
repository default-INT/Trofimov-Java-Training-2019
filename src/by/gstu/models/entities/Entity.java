package by.gstu.models.entities;

import by.gstu.models.utils.ParserJSON;
import org.json.JSONObject;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
public abstract class Entity implements ParserJSON {

	public Entity(int id) {
		super();
		this.id = id;
	}
	
	public Entity() {
		
	}

	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject returnReqJson = new JSONObject();

		returnReqJson.put("id", id);

		return returnReqJson;
	}
}
