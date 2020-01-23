package by.gstu.models.entities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;

/**
 * Entity class.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class Car extends Entity {

	private String model;
	private int mileage;
	private int yearOfIssue;
	private double priceHour;
	private int transmissionId;
	private Transmission transmission;
	private String number;
	private int fuelTypeId;
	private FuelType fuelType;

	public Car(int id, String model, int mileage, int yearOfIssue, double priceHour, int transmissionId,
			   String number, int fuelTypeId) {
		super(id);
		this.model = model;
		this.mileage = mileage;
		this.yearOfIssue = yearOfIssue;
		this.priceHour = priceHour;
		this.transmissionId = transmissionId;
		this.number = number;
		this.fuelTypeId = fuelTypeId;
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
	public Transmission getTransmission() {
		//TODO: get from database. Support DAO
		return transmission;
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
	public FuelType getFuelType() {
		//TODO: get from database. Support DAO
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
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

		return carJson;
	}

	@Override
	public String toString() {
		return getId() + "_" + getModel() + "_" + getMileage() + "_" + getYearOfIssue() + "_" + getPriceHour();
	}

	public abstract static class CarEntity extends Entity {
		protected String name;

		public CarEntity(int id, String name) {
			super(id);
			this.name = name;
		}

		public CarEntity(String name) {
			this.name = name;
		}

		public CarEntity() {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public JSONObject toJSON() {
			JSONObject carJson = new JSONObject();

			carJson.put("name", getName());

			return carJson;
		}
	}

	public static class FuelType extends CarEntity {
		public FuelType(int id, String name) {
			super(id, name);
		}

		public FuelType(String name) {
			super(name);
		}

		public FuelType() {
		}
	}

	public static class Transmission extends CarEntity {

		public Transmission(int id, String name) {
			super(id, name);
		}

		public Transmission(String name) {
			super(name);
		}

		public Transmission() {
		}
	}
}
