package by.gstu.models.entities;

import by.gstu.models.utils.ParserJSON;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
@MappedSuperclass
public abstract class Entity implements ParserJSON, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	protected int id;

	public Entity(int id) {
		super();
		this.id = id;
	}
	
	public Entity() {
	}

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
