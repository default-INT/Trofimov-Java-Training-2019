package by.gstu.models.untils;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * <h1>Configuration manager for read data from .property files.</h1>
 * Implemented singelton pattern.
 *
 * @author Evgeniy Trofimov
 * @version 1.3
 */
public class ConfigurationManager {

    private static final Logger logger = Logger.getLogger(ConfigurationManager.class);

    private static ConfigurationManager instance;

    private ConfigurationManager() {
    }

    public synchronized static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Read data from default .property file on key.
     *
     * @param key
     * @return data on key
     */
    public String getProperty(String key) {
        return getProperty(key, "default", "config");
    }

    /**
     * Read data from default .property file on key.
     * If key not found, return defaultValue.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        return getProperty(key, defaultValue, "config");
    }

    /**
     * Read data from (filePart).property file on key. If (filePart).property not found, then try founded key in
     * config.property and if failed find this key, then get default value.
     *
     * @param key
     * @param defaultValue
     * @param filePart
     * @return
     */
    public String getProperty(String key, String defaultValue, String filePart) {
        filePart = filePart.toLowerCase();
        try(FileInputStream fileInputStream = new FileInputStream("/resources/" + filePart +".properties")) {
            Properties prop = new Properties();
            prop.load(fileInputStream);
            return prop.getProperty(key, defaultValue);
        }catch (FileNotFoundException e) {
            logger.warn("Not found " + filePart + ".property file in resources, try search in config.property. " +
                    "Exception: " + e.getMessage());
            try(FileInputStream fileInputStream = new FileInputStream("/resources/config.properties")) {
                Properties prop = new Properties();
                prop.load(fileInputStream);
                return prop.getProperty(key, defaultValue);
            } catch (Exception ex) {
                logger.warn( "Not found property file in config.property, give default value. Error: "
                        + e.getMessage(), e);
            }
        }catch (Exception e){
            logger.warn( "Not found property file, give default value. Error: " + e.getMessage(), e);
        }
        return defaultValue;
    }
}
