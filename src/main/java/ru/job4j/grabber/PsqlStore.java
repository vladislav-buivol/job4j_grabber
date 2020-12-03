package ru.job4j.grabber;

import ru.job4j.grabber.quartz.model.Post;
import ru.job4j.grabber.utils.properties.reader.PostgresPropertiesReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;
    private static final String POST_TABLE = "posts";

    public static void main(String[] args) {
        PsqlStore store = new PsqlStore(new PostgresPropertiesReader().readProperties());
        SqlRuParse parse = new SqlRuParse();
        String link = "https://www.sql.ru/forum/job-offers";
        parse.list(link).forEach(store::save);
        System.out.println(store.findById("37"));
        System.out.println(store.getAll());
    }

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            this.cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(Post post) {
        try (final PreparedStatement statement = this.cnn.
                prepareStatement("insert into posts(name,text,link,created) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.name());
            statement.setString(2, post.postText());
            statement.setString(3, post.link());
            statement.setTimestamp(4, new Timestamp(post.created().getTimeInMillis()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (final PreparedStatement statement = this.cnn.
                prepareStatement(String.format("select * from %s", POST_TABLE), Statement.RETURN_GENERATED_KEYS)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getTimestamp("created"));
                Post post = new Post(result.getString("name"),
                        result.getString("text"),
                        cal,
                        result.getString("link"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        try (final PreparedStatement statement = this.cnn.
                prepareStatement(String.format("select * from %s where id = ?", POST_TABLE), Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, Integer.parseInt(id));
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(result.getTimestamp("created"));
                return new Post(result.getString("name"),
                        result.getString("text"),
                        cal,
                        result.getString("link"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}