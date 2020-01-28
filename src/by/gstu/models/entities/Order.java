package by.gstu.models.entities;

import by.gstu.models.dao.DAOFactory;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class Order extends Entity {

	private Calendar orderDate;
	private int period;
	private Calendar returnDate;
	private int carId;
	private int clientId;
	private String passportData;
	private double price;

	private Car car;
	private Client client;

	public Order(int id, Calendar orderDate, int period, Calendar returnDate, String phoneNumber, double price,
				 Car car, Client client) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.passportData = phoneNumber;
		this.price = price;
		this.car = car;
		this.client = client;
	}

	public Order(int id, Calendar orderDate, int period, Calendar returnDate, int clientId, int carId,
				 String passportData, double price) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.carId = carId;
		this.clientId = clientId;
		this.passportData = passportData;
		this.price = price;
	}

	public Order() {
	}

	public Calendar getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Calendar orderDate) {
		this.orderDate = orderDate;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public Calendar getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Calendar returnDate) {
		this.returnDate = returnDate;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public String getPassportData() {
		return passportData;
	}
	public void setPassportData(String passportData) {
		this.passportData = passportData;
	}
	public void setCar(Car car) {
		this.car = car;
	}

	public Car getCar() {
		if (car == null) {
			DAOFactory dao = DAOFactory.getDAOFactory();
			car = dao.getCarDAO().read(carId);
		}
		return car;
	}

	public Client getClient() {
		if (client == null) {
			DAOFactory dao = DAOFactory.getDAOFactory();
			client = (Client) dao.getAccountDAO().read(clientId);
		}
		return client;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject orderJson = super.toJSON();

		orderJson.put("orderDate", orderDate);
		orderJson.put("period", period);
		orderJson.put("returnDate", returnDate);
		orderJson.put("car", getCar().toJSON());
		orderJson.put("client", getClient().toJSON());
		orderJson.put("passportData", getClient().toJSON());
		orderJson.put("price", price);

		return orderJson;
	}
}
