package by.gstu.models.utils;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <h1>Connection pool</h1>
 *
 * <p>This class storage "connection" to database in synchronized collection BlockingQueue and transmits "Connection"
 * in methods in DAO layer.</p>
 *
 * <p>Implements "Singelton" pattern.</p>
 *
 * @author Evgeni Trofimov
 * @version 1.0
 */
public class ConnectionPool {

    private static final Logger logger = Logger.getLogger(ConnectionPool.class);

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    private static final int POOL_SIZE;
    private static ConnectionPool instance;
    private BlockingQueue<Connection> connections;

    /**
     * Read parameters for connecting to database.
     */
    static {
        ConfigurationManager configurateManager = ConfigurationManager.getInstance();
        URL = configurateManager.getProperty("db.url", "jdbc:mysql://localhost:3306/cars_rental?&serverTimezone=UTC");
        USER = configurateManager.getProperty("db.user", "root");
        PASSWORD = configurateManager.getProperty("db.password", "1806");
        POOL_SIZE = Integer.parseInt(configurateManager.getProperty("db.poolSize", "10"));
    }

    /**
     * Return instance "ConnectionPool". If instance equally null. Create new instance.
     *
     * @return ConnectionPool
     */
    public synchronized static ConnectionPool getInstance() {
        if (instance == null) {
            logger.info("create connection pool");
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * Create "POOL_SIZE" "Connecting" and added in ArrayBlockingQueue
     */
    private ConnectionPool(){
        connections = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++){
            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(URL,  USER, PASSWORD);
                connections.offer(connection);
            } catch (SQLException e) {
                logger.fatal(e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                logger.fatal(e.getMessage(), e);
            }
        }
    }

    /**
     * Return "Connection" from queue.
     *
     * @return
     * @throws InterruptedException
     */
    public Connection getConnection() throws InterruptedException {
        return connections.take();
    }

    /**
     * "Connection" comes back in queue.
     *
     * @param connection
     */
    public void closeConnection(Connection connection) {
        if (connection != null){
            connections.offer(connection);
        }
    }
}
