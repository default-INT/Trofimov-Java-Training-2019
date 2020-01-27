package by.gstu.models.dao.mysql;

import by.gstu.models.dao.CarDAO;
import by.gstu.models.entities.Car;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements CarDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.0
 */
public class MySqlCarDAO implements CarDAO {

    private static final Logger logger = Logger.getLogger(MySqlCarDAO.class);

    private static final String DEFAULT_CREATE = "CALL add_car(?, ?, ?, ?, ?, ?, ?)";
    private static final String DEFAULT_READ = "CALL read_car(?)";
    private static final String DEFAULT_READ_ALL = "CALL read_all_cars()";
    private static final String DEFAULT_UPDATE = "CALL edit_car(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DEFAULT_DELETE = "CALL delete_car(?)";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;

    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();
        CREATE = configurateManager.getProperty("sql.Cars.create", DEFAULT_CREATE, "mysql");
        READ = configurateManager.getProperty("sql.Cars.read", DEFAULT_READ, "mysql");
        READ_ALL = configurateManager.getProperty("sql.Cars.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configurateManager.getProperty("sql.Cars.update", DEFAULT_UPDATE, "mysql");
        DELETE = configurateManager.getProperty("sql.Cars.delete", DEFAULT_DELETE, "mysql");
    }

    public MySqlCarDAO() {
    }

    @Override
    public boolean create(Car car) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();

            pStatement = connection.prepareStatement(CREATE);
            pStatement.setString(1, car.getNumber());
            pStatement.setString(2, car.getModel());
            pStatement.setInt(3, car.getMileage());
            pStatement.setInt(4, car.getYearOfIssue());
            pStatement.setDouble(5, car.getPriceHour());
            pStatement.setInt(6, car.getTransmissionId());
            pStatement.setInt(7, car.getFuelTypeId());

            int k = pStatement.executeUpdate();
            if (k > 0) return true;
            return false;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Collection<Car> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<Car> cars = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String number = resultSet.getString("number");
                String model = resultSet.getString("model");
                int mileage = resultSet.getInt("mileage");
                int yearOfIssue = resultSet.getInt("year_of_issue");
                double priceHour = resultSet.getDouble("price_hour");
                int transmissionId = resultSet.getInt("transmission_id");
                int fuelTypeId = resultSet.getInt("fuel_type_id");

                cars.add(new Car(id, model, mileage, yearOfIssue, priceHour, transmissionId, number, fuelTypeId));
            }
            return cars;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public Car read(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(READ);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String number = resultSet.getString("number");
                String model = resultSet.getString("model");
                int mileage = resultSet.getInt("mileage");
                int yearOfIssue = resultSet.getInt("year_of_issue");
                double priceHour = resultSet.getDouble("price_hour");
                int transmissionId = resultSet.getInt("transmission_id");
                int fuelTypeId = resultSet.getInt("fuel_type_id");

                return new Car(id, model, mileage, yearOfIssue, priceHour, transmissionId, number, fuelTypeId);
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Car car) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement;
        try {
            connection = connectionPool.getConnection();
            pStatement = connection.prepareStatement(UPDATE);
            pStatement.setInt(1, car.getId());
            pStatement.setString(2, car.getNumber());
            pStatement.setString(3, car.getModel());
            pStatement.setInt(4, car.getMileage());
            pStatement.setInt(5, car.getYearOfIssue());
            pStatement.setDouble(6, car.getPriceHour());
            pStatement.setInt(7, car.getTransmissionId());
            pStatement.setInt(8, car.getFuelTypeId());

            int k = pStatement.executeUpdate();
            if (k > 0) return true;
            return false;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        PreparedStatement pStatement = null;
        try {
            connection = connectionPool.getConnection();
            pStatement = connection.prepareStatement(DELETE);
            pStatement.setInt(1, id);

            int k = pStatement.executeUpdate();
            if (k > 0) return true;
            return false;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    public static class MySqlTransmissionDAO implements CarDAO.CarEntityDAO<Car.Transmission> {

        private static final String DEFAULT_READ_TRANSMISSION = "CALL read_transmission(?)";
        private static final String DEFAULT_READ_ALL_TRANSMISSION = "CALL read_all_transmissions()";

        private static final String READ_TRANSMISSION;
        private static final String READ_ALL_TRANSMISSION;

        static {
            ConfigurationManager configurateManager = ConfigurationManager.getInstance();
            READ_TRANSMISSION = configurateManager.getProperty("sql.Transmissions.read", DEFAULT_READ_TRANSMISSION,
                    "mysql");
            READ_ALL_TRANSMISSION = configurateManager.getProperty("sql.Transmissions.readAll",
                    DEFAULT_READ_ALL_TRANSMISSION, "mysql");
        }

        @Override
        public Collection<Car.Transmission> readAll() {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareStatement(READ_ALL_TRANSMISSION);
                ResultSet resultSet = statement.executeQuery();
                Collection<Car.Transmission> transmissions = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    transmissions.add(new Car.Transmission(id, name));
                }
                return transmissions;
            } catch (SQLException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                connectionPool.closeConnection(connection);
            }
            return null;
        }

        @Override
        public Car.Transmission read(int id) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareStatement(READ_TRANSMISSION);
                statement.setInt(1, id);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString("name");

                    return new Car.Transmission(id, name);
                }
            } catch (SQLException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                connectionPool.closeConnection(connection);
            }
            return null;
        }
    }

    public static class MySqlFuelTypeDAO implements CarDAO.CarEntityDAO<Car.FuelType> {

        private static final String DEFAULT_READ_FUEL_TYPE = "CALL read_all_fuel_types()";
        private static final String DEFAULT_READ_ALL_FUEL_TYPE = "CALL read_fuel_type(?)";

        private static final String READ_FUEL_TYPE;
        private static final String READ_ALL_FUEL_TYPE;

        static {
            ConfigurationManager configurateManager = ConfigurationManager.getInstance();
            READ_FUEL_TYPE = configurateManager.getProperty("sql.FuelTypes.read", DEFAULT_READ_FUEL_TYPE,
                    "mysql");
            READ_ALL_FUEL_TYPE = configurateManager.getProperty("sql.FuelTypes.readAll",
                    DEFAULT_READ_ALL_FUEL_TYPE, "mysql");
        }

        @Override
        public Collection<Car.FuelType> readAll() {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareStatement(READ_ALL_FUEL_TYPE);
                ResultSet resultSet = statement.executeQuery();
                Collection<Car.FuelType> fuelTypes = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    fuelTypes.add(new Car.FuelType(id, name));
                }
                return fuelTypes;
            } catch (SQLException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                connectionPool.closeConnection(connection);
            }
            return null;
        }

        @Override
        public Car.FuelType read(int id) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            PreparedStatement statement = null;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareStatement(READ_ALL_FUEL_TYPE);
                statement.setInt(1, id);

                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String name = resultSet.getString("name");

                    return new Car.FuelType(id, name);
                }
            } catch (SQLException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                connectionPool.closeConnection(connection);
            }
            return null;
        }
    }
}
