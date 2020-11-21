package quartz;

import java.sql.Connection;
import java.sql.DriverManager;

public class RabbitDataBase {

    public static Connection connect() {
        try {
            Class.forName(RabbitProperties.driver());
            return DriverManager.getConnection(RabbitProperties.url(), RabbitProperties.username(), RabbitProperties.password());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot connect");
    }
}
