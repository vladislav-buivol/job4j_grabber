package ru.job4j.grabber.utils.properties.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PostgresPropertiesReader {
    private static String propertiesLocation = System.getProperty("user.dir") + "/src/main/resources/aggregator/postgres.properties".replace("/", File.separator);
    static Properties properties = new Properties();
    private static final String DRIVER = "jdbc.driver";
    private static final String URL = "jdbc.url";
    private static final String USERNAME = "jdbc.username";
    private static final String PASSWORD = "jdbc.password";

    public Properties readProperties() {
        try {
            InputStream stream = new FileInputStream(propertiesLocation);
            properties.load(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String driver() {
        return String.valueOf(properties.get(DRIVER));
    }

    public static String url() {
        return String.valueOf(properties.get(URL));
    }

    public static String login() {
        return String.valueOf(properties.get(USERNAME));
    }

    public static String password() {
        return String.valueOf(properties.get(PASSWORD));
    }

    public static String getByKey(String key) {
        return String.valueOf(properties.get(key));
    }

    public static String propertiesLocation() {
        return propertiesLocation;
    }
}
