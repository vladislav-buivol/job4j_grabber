package ru.job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {

    public static void main(String[] args) {
        try (Connection store = RabbitDataBase.connect()) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            System.out.println(store);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }


    public static class Rabbit implements Job {
        private static final String TABLE = "rabbit";

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection store = (Connection) context.getJobDetail().getJobDataMap().get("store");
            insertData(store);
        }

        private void insertData(Connection connection) {
            String query = String.format("INSERT INTO %s(name) VALUES (?)", TABLE);
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "Rabbit");
                statement.execute();
                System.out.println("Rabbit was added");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}