package quartz;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class RabbitProperties {
    private static final String PROPERTIES_LOCATION = "src/main//java/quartz/properties/rabbit.properties";
    private static final Properties PROPERTIES = new Properties();
    private static final String INTERVAL = "rabbit.interval";

    protected static Properties readProperties() {
        try {
            InputStream stream = new FileInputStream(PROPERTIES_LOCATION);
            PROPERTIES.load(stream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return PROPERTIES;
    }

    protected static int interval() {
        return Integer.parseInt(PROPERTIES.getProperty(INTERVAL));
    }

    protected static String get(String key) {
        return String.valueOf(PROPERTIES.get(key));
    }


}
