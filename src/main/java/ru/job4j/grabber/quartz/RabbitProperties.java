package ru.job4j.grabber.quartz;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class RabbitProperties {
    private static final String PROPERTIES_LOCATION = "src/main//java/ru.job4j.grabber.quartz/properties/rabbit.properties";
    private static Properties properties = null;
    private static final String INTERVAL = "rabbit.interval";
    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER = "driver-class-name";

    protected static Properties readProperties() {
        if (properties != null) {
            return properties;
        }
        try {
            properties = new Properties();
            InputStream stream = new FileInputStream(PROPERTIES_LOCATION);
            properties.load(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    protected static int interval() {
        readProperties();
        return Integer.parseInt(properties.getProperty(INTERVAL));
    }

    public static String url() {
        readProperties();
        return String.valueOf(properties.get(URL));
    }

    public static String username() {
        readProperties();
        return String.valueOf(properties.get(USERNAME));
    }

    public static String password() {
        readProperties();
        return String.valueOf(properties.get(PASSWORD));
    }

    public static String driver() {
        readProperties();
        return String.valueOf(properties.get(DRIVER));
    }

    protected static String get(String key) {
        readProperties();
        return String.valueOf(properties.get(key));
    }

}
