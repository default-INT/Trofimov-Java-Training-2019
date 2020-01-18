package by.gstu.models.entities;

import by.gstu.models.untils.ParserJSON;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
public abstract class Entity implements ParserJSON {

	public Entity(int id) {
		super();
		id = id;
	}
	
	public Entity() {
		
	}

	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		id = id;
	} 
}
