package by.gstu.models.entities.car;

import by.gstu.models.dao.DAOFactory;
import by.gstu.models.dao.mysql.MySqlDAOFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
@javax.persistence.Entity
@Table(name = "cars")
public class Car extends by.gstu.models.entities.Entity {

	private String model;
	private int mileage;
	@Column(name = "year_of_issue")
	private int yearOfIssue;
	@Column(name = "price_hour")
	private double priceHour;
	@Column(name = "transmission_id", updatable = false, insertable = false)
	private int transmissionId;
	private String number;
	@Column(name = "fuel_type_id", updatable = false, insertable = false)
	private int fuelTypeId;
	private boolean available;

	@ManyToOne
	@JoinColumn(name = "transmission_id")
	private Transmission transmission;
	@ManyToOne
	@JoinColumn(name = "fuel_type_id")
	private FuelType fuelType;

	public Car(int id, String model, int mileage, int yearOfIssue, double priceHour, int transmissionId,
			   String number, int fuelTypeId, boolean available) {
		super(id);
		this.model = model;
		this.mileage = mileage;
		this.yearOfIssue = yearOfIssue;
		this.priceHour = priceHour;
		this.transmissionId = transmissionId;
		this.number = number;
		this.fuelTypeId = fuelTypeId;
		this.available = available;
	}

	public Car(int id) {
		super(id);
	}

	public Car() {

	}

	/**
	 * <h1>Convert car to json object.<h1/>
	 * @param car
	 * @return JSON car
	 */
	public static JSONObject toJSON(Car car) throws ClassNotFoundException {
		return car.toJSON();
	}

	/**
	 * <h1>Convert collection cars to json object</h1>
	 * @param cars
	 * @return
	 */
	public static JSONArray toJSONArray(Collection<Car> cars) {
		JSONArray jsonCars = new JSONArray();
		for (Car car : cars)
			jsonCars.put(car.toJSON());
		return  jsonCars;
	}

	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public int getYearOfIssue() {
		return yearOfIssue;
	}
	public void setYearOfIssue(int yearOfIssue) {
		this.yearOfIssue = yearOfIssue;
	}
	public double getPriceHour() {
		return priceHour;
	}
	public void setPriceHour(double priceHour) {
		this.priceHour = priceHour;
	}
	public int getTransmissionId() {
		return transmissionId;
	}
	public void setTransmissionId(int transmissionId) {
		this.transmissionId = transmissionId;
	}
	public int getFuelTypeId() {
		return fuelTypeId;
	}
	public void setFuelTypeId(int fuelTypeId) {
		this.fuelTypeId = fuelTypeId;
	}
	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public FuelType getFuelType() {
		if (fuelType == null) {
			DAOFactory dao = new MySqlDAOFactory();
			fuelType = dao.getFuelTypeDAO().read(fuelTypeId);
		}
		return fuelType;
	}

	public Transmission getTransmission() {
		if (transmission == null) {
			DAOFactory dao = new MySqlDAOFactory();
			transmission = dao.getTransmissionDAO().read(transmissionId);
		}
		return transmission;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject carJson = new JSONObject();

		carJson.put("id", id);
		carJson.put("model", model);
		carJson.put("mileage", mileage);
		carJson.put("yearOfIssue", yearOfIssue);
		carJson.put("priceHour", priceHour);
		carJson.put("transmission", getTransmission().getName());
		carJson.put("number", number);
		carJson.put("fuelType", getFuelType().getName());
		carJson.put("available", available);

		return carJson;
	}

	@Override
	public String toString() {
		return getId() + "_" + getModel() + "_" + getMileage() + "_" + getYearOfIssue() + "_" + getPriceHour();
	}
}
