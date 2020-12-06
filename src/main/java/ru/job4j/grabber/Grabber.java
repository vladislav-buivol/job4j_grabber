package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.quartz.model.Post;
import ru.job4j.grabber.utils.properties.reader.PostgresPropertiesReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties cfg = new Properties();
    private final String link;
    private final String propertiesLocation;
    private Store store;
    private Predicate<String> saveCondition;

    public Grabber(String link, String propertiesLocation, Predicate<String> saveCondition) {
        this.link = link;
        this.propertiesLocation = propertiesLocation;
        this.saveCondition = saveCondition;
    }

    public Store store() {
        if (store == null) {
            this.store = new PsqlStore(cfg);
        }
        return this.store;
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() throws IOException {
        try (InputStream in = new FileInputStream(new File(propertiesLocation))) {
            cfg.load(in);
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        data.put("link", link);
        data.put("saveCondition", saveCondition);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            String link = (String) map.get("link");
            Predicate<String> saveCondition = (Predicate<String>) map.get("saveCondition");
            parse.list(link).forEach(el -> save(store, el, saveCondition));
        }

        private void save(Store store, Post el, Predicate<String> condition) {
            if (condition == null) {
                store.save(el);
            } else if (condition.test(el.name().toLowerCase())) {
                store.save(el);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        String link = "https://www.sql.ru/forum/job-offers/1";
        Predicate<String> saveCondition = Pattern.compile("\\b(java[^A-Za-z])|(java)\\b").asPredicate();
        Grabber grab = new Grabber(link, PostgresPropertiesReader.propertiesLocation(), saveCondition);
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
    }
}