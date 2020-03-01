package by.gstu.models.entities;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.entities.car.Car;
import by.gstu.models.utils.ParserJSON;
import org.hibernate.annotations.NamedQuery;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.1
 */
@javax.persistence.Entity
@Table(name = "orders")
@NamedQuery(
		name = "getOrdersByClintId",
		query = "SELECT o FROM Order o WHERE o.clientId = :clientId")
@NamedQuery(
		name = "closeOrder",
		query = "UPDATE Order SET closed = true WHERE id = :orderId")
public class Order extends Entity {

	@Column(name = "order_date")
	private Calendar orderDate;
	private int period;
	@Column(name = "return_date")
	private Calendar returnDate;
	@Column(name = "car_id", updatable = false, insertable = false)
	private int carId;
	@Column(name = "client_id", updatable = false, insertable = false)
	private int clientId;
	@Column(name = "passport_data")
	private String passportData;
	private double price;
	private boolean closed;

	@ManyToOne
	@JoinColumn(name = "car_id")
	private Car car;
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;
	@OneToOne(optional = false, mappedBy = "order")
	private ReturnRequest returnRequest;

	/**
	 *
	 * @param id
	 * @param orderDate
	 * @param period
	 * @param returnDate
	 * @param phoneNumber
	 * @param price
	 * @param car
	 * @param client
	 * @param closed
	 */
	public Order(int id, Calendar orderDate, int period, Calendar returnDate, String phoneNumber, double price,
                 Car car, Client client, boolean closed) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.passportData = phoneNumber;
		this.price = price;
		this.car = car;
		this.client = client;
		this.closed = closed;
	}

	/**
	 *
	 * @param id
	 * @param orderDate
	 * @param period
	 * @param returnDate
	 * @param clientId
	 * @param carId
	 * @param passportData
	 * @param price
	 * @param closed
	 */
	public Order(int id, Calendar orderDate, int period, Calendar returnDate, int clientId, int carId,
				 String passportData, double price, boolean closed) {
		super(id);
		this.orderDate = orderDate;
		this.period = period;
		this.returnDate = returnDate;
		this.carId = carId;
		this.clientId = clientId;
		this.passportData = passportData;
		this.price = price;
		this.closed = closed;
	}

	/**
	 *
	 * @param orderDate
	 * @param rentalPeriod
	 * @param carId
	 * @param clientId
	 * @param passportData
	 * @param price
	 */
	public Order(Calendar orderDate, int rentalPeriod, int carId, int clientId, String passportData, double price) {
		this.orderDate = orderDate;
		this.period = rentalPeriod;
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
	public boolean isClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	public ReturnRequest getReturnRequest() {
		return returnRequest;
	}
	public void setReturnRequest(ReturnRequest returnRequest) {
		this.returnRequest = returnRequest;
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

		orderJson.put("orderDate", ParserJSON.parseCalendar(orderDate));
		orderJson.put("period", period);
		orderJson.put("returnDate", ParserJSON.parseCalendar(returnDate));
		orderJson.put("car", getCar().toJSON());
		orderJson.put("client", getClient().toJSON());
		orderJson.put("passportData", getClient().toJSON());
		orderJson.put("price", price);
		orderJson.put("closed", closed);
		orderJson.put("carName", getCar().getModel());

		return orderJson;
	}
}
