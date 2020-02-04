package by.gstu.models.entities;

import org.json.JSONObject;

/**
 * Entity class.
 * Extends Account.
 *
 * @author Evgeniy Trofimov
 * @version 2.1
 */
public class Client extends Account {

	private int birthdayYear;

	/**
	 *
	 * @param id {int}
	 * @param login {String}
	 * @param password {String}
	 * @param email {String}
	 * @param fullName {String}
	 * @param birthdayYear {String}
	 */
	public Client(int id, String login, String password, String email, String fullName, int birthdayYear) {
		super(id, login, password, email, fullName);
		this.birthdayYear = birthdayYear;
	}

	/**
	 *
	 * @param login
	 * @param password
	 * @param email
	 * @param fullName
	 * @param birthdayYear
	 */
	public Client(String login, String password, String email, String fullName, int birthdayYear) {
		super(login, password, email, fullName);
		this.birthdayYear = birthdayYear;
	}

	public Client() {
	}

	public Client(int id, String login, String email, String fullName, int birthdayYear) {
		super(id, login, email, fullName);
		this.birthdayYear = birthdayYear;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject clientJson = super.toJSON();

		clientJson.put("birthdayYear", birthdayYear);
		clientJson.put("status", "client");

		return clientJson;
	}

	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public int getBirthdayYear() {
		return birthdayYear;
	}
	public void setBirthdayYear(int bithdayYear) {
		this.birthdayYear = bithdayYear;
	}
}
