package by.gstu.models.utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <h1>Configuration manager for read data from .property files.</h1>
 * Implemented singelton pattern.
 *
 * @author Evgeniy Trofimov
 * @version 1.5
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
     *  <h1>Read all urls from .properties file. Value including: url path - file path.</h1>
     *
     * @param filePath - path to properties file who include key - value (url path - file path)
     * @return HashMap<String, String> - <url, filePath>
     */
    public Map<String, String> getUrlsPath(String filePath) {
        filePath = filePath.toLowerCase();
        try(InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream(filePath +".properties")) {
            Properties prop = new Properties();
            prop.load(inputStream);
            Map<String, String> urlsPath = new HashMap<>();
            prop.stringPropertyNames().forEach((name) -> urlsPath.put(name, prop.getProperty(name)));
            return urlsPath;
        } catch (FileNotFoundException e) {
            logger.warn("Not found " + filePath + ".property file in resources, try search in config.property. " +
                    "Exception: " + e.getMessage());
            try(InputStream inputStream = getClass()
                    .getClassLoader().getResourceAsStream("urls.properties")) {
                Properties prop = new Properties();
                prop.load(inputStream);
                Map<String, String> urlsPath = new HashMap<>();
                prop.stringPropertyNames().forEach((name) -> urlsPath.put(name, prop.getProperty(name)));
                return urlsPath;
            } catch (Exception ex) {
                logger.warn( "Not found property file in config.property, give default value. Error: "
                        + e.getMessage(), e);
            }
        }catch (Exception e) {
            logger.warn( "Not found property file, give default value. Error: " + e.getMessage(), e);
        }
        return null;
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
        try(InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream(filePart +".properties")) {
            Properties prop = new Properties();
            prop.load(inputStream);
            return prop.getProperty(key, defaultValue);
        } catch (FileNotFoundException e) {
            logger.warn("Not found " + filePart + ".property file in resources, try search in config.property. " +
                    "Exception: " + e.getMessage());
            try(InputStream inputStream = getClass()
                    .getClassLoader().getResourceAsStream("config.properties")) {
                Properties prop = new Properties();
                prop.load(inputStream);
                return prop.getProperty(key, defaultValue);
            } catch (Exception ex) {
                logger.warn( "Not found property file in config.property, give default value. Error: "
                        + e.getMessage(), e);
            }
        }catch (Exception e) {
            logger.warn( "Not found property file, give default value. Error: " + e.getMessage(), e);
        }
        return defaultValue;
    }
}
