package by.gstu.models.entities;

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
	private String phoneNumber;
	private double price;

	private Car car;
	private Client client;

	public Order(int id, Calendar orderDate, int period, Calendar returnDate, String phoneNumber, double price, Car car, Client client) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.phoneNumber = phoneNumber;
		this.price = price;
		this.car = car;
		this.client = client;
	}

	public Order(int id, Calendar orderDate, int period, Calendar returnDate, int carId, int clientId, String phoneNumber, double price) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.carId = carId;
		this.clientId = clientId;
		this.phoneNumber = phoneNumber;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Car getCar() {
		//TODO: get from DAO
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public Client getClient() {
		//TODO: get from DAO
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public JSONObject toJSON() {
		return null;
	}
}
