package by.gstu.models.dao.mysql;

import by.gstu.models.dao.CarDAO;
import by.gstu.models.entities.Car;
import by.gstu.models.untils.ConfigurationManager;
import by.gstu.models.untils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements CarDAO.
 *
 * @author Evgeniy Trofimov
 * @version 2.1
 */
class MySqlCarDAO implements CarDAO {

    private static final Logger logger = Logger.getLogger(MySqlCarDAO.class);

    private static final String DEFAULT_CREATE = "{CALL add_car(?, ?, ?, ?, ?, ?, ?)}";
    private static final String DEFAULT_READ = "{CALL read_car(?)}";
    private static final String DEFAULT_READ_ALL = "{CALL read_all_cars()}";
    private static final String DEFAULT_UPDATE = "{CALL edit_car(?, ?, ?, ?, ?, ?, ?, ?)}";
    private static final String DEFAULT_DELETE = "{CALL delete_car(?)}";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;

    static {
        ConfigurationManager configuratorManager = ConfigurationManager.getInstance();
        CREATE = configuratorManager.getProperty("sql.Cars.create", DEFAULT_CREATE, "mysql");
        READ = configuratorManager.getProperty("sql.Cars.read", DEFAULT_READ, "mysql");
        READ_ALL = configuratorManager.getProperty("sql.Cars.readAll", DEFAULT_READ_ALL, "mysql");
        UPDATE = configuratorManager.getProperty("sql.Cars.update", DEFAULT_UPDATE, "mysql");
        DELETE = configuratorManager.getProperty("sql.Cars.delete", DEFAULT_DELETE, "mysql");
    }

    public MySqlCarDAO() {
    }

    @Override
    public boolean create(Car car) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(CREATE);
            callStatement.setString(1, car.getNumber());
            callStatement.setString(2, car.getModel());
            callStatement.setInt(3, car.getMileage());
            callStatement.setInt(4, car.getYearOfIssue());
            callStatement.setDouble(5, car.getPriceHour());
            callStatement.setInt(6, car.getTransmissionId());
            callStatement.setInt(7, car.getFuelTypeId());

            int k = callStatement.executeUpdate();
            return k > 0;
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
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ_ALL);
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
                boolean available = resultSet.getBoolean("available");

                cars.add(new Car(id, model, mileage, yearOfIssue, priceHour, transmissionId, number, fuelTypeId,
                        available));
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
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ);
            statement.setInt("var_id", id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String number = resultSet.getString("number");
                String model = resultSet.getString("model");
                int mileage = resultSet.getInt("mileage");
                int yearOfIssue = resultSet.getInt("year_of_issue");
                double priceHour = resultSet.getDouble("price_hour");
                int transmissionId = resultSet.getInt("transmission_id");
                int fuelTypeId = resultSet.getInt("fuel_type_id");
                boolean available = resultSet.getBoolean("available");

                return new Car(id, model, mileage, yearOfIssue, priceHour, transmissionId, number, fuelTypeId,
                        available);
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
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();
            callStatement = connection.prepareCall(UPDATE);
            callStatement.setInt("var_id", car.getId());
            callStatement.setString("var_number", car.getNumber());
            callStatement.setString("var_model", car.getModel());
            callStatement.setInt("var_mileage", car.getMileage());
            callStatement.setInt("var_year_of_issue", car.getYearOfIssue());
            callStatement.setDouble("var_price_hour", car.getPriceHour());
            callStatement.setInt("var_transmission_id", car.getTransmissionId());
            callStatement.setInt("var_fuel_type_id", car.getFuelTypeId());

            int k = callStatement.executeUpdate();
            return k > 0;
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
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();
            callStatement = connection.prepareCall(DELETE);
            callStatement.setInt("var_id", id);

            int k = callStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    public static class MySqlTransmissionDAO implements CarDAO.CarEntityDAO<Car.Transmission> {

        private static final String DEFAULT_READ_TRANSMISSION = "{CALL read_transmission(?)}";
        private static final String DEFAULT_READ_ALL_TRANSMISSION = "{CALL read_all_transmissions()}";

        private static final String READ_TRANSMISSION;
        private static final String READ_ALL_TRANSMISSION;

        static {
            ConfigurationManager configuratorManager = ConfigurationManager.getInstance();
            READ_TRANSMISSION = configuratorManager.getProperty("sql.Transmissions.read", DEFAULT_READ_TRANSMISSION,
                    "mysql");
            READ_ALL_TRANSMISSION = configuratorManager.getProperty("sql.Transmissions.readAll",
                    DEFAULT_READ_ALL_TRANSMISSION, "mysql");
        }

        @Override
        public Collection<Car.Transmission> readAll() {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            CallableStatement statement;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareCall(READ_ALL_TRANSMISSION);
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
            CallableStatement statement;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareCall(READ_TRANSMISSION);
                statement.setInt("var_id", id);

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

        private static final String DEFAULT_READ_FUEL_TYPE = "{CALL read_all_fuel_types()}";
        private static final String DEFAULT_READ_ALL_FUEL_TYPE = "{CALL read_fuel_type(?)}";

        private static final String READ_FUEL_TYPE;
        private static final String READ_ALL_FUEL_TYPE;

        static {
            ConfigurationManager configuratorManager = ConfigurationManager.getInstance();
            READ_FUEL_TYPE = configuratorManager.getProperty("sql.FuelTypes.read", DEFAULT_READ_FUEL_TYPE,
                    "mysql");
            READ_ALL_FUEL_TYPE = configuratorManager.getProperty("sql.FuelTypes.readAll",
                    DEFAULT_READ_ALL_FUEL_TYPE, "mysql");
        }

        @Override
        public Collection<Car.FuelType> readAll() {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = null;
            CallableStatement statement;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareCall(READ_ALL_FUEL_TYPE);
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
            CallableStatement statement;
            try {
                connection = connectionPool.getConnection();
                statement = connection.prepareCall(READ_FUEL_TYPE);
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
