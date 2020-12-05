package ru.job4j.grabber.quartz.model;

import ru.job4j.grabber.PsqlStore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Post {
    private final String name;
    private final String link;
    //private final String created;
    private final Calendar created;
    private final String postText;

    public Post(PostTopic topic, PostDate date) {
        this.name = topic.name();
        this.link = topic.href();
        this.postText = topic.postText();
        this.created = date.calendarDate();
    }

    public Post(String name, String link, Calendar date, String postText) {
        this.name = name;
        this.link = link;
        this.postText = postText;
        this.created = date;
    }

    public String name() {
        return this.name;
    }

    public String link() {
        return this.link;
    }

    public Calendar created() {
        return this.created;
    }

    public String postText() {
        return this.postText;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", new Locale("ru"));
        return "Post{"
                + "name='" + name + '\''
                + ", link='" + link + '\''
                + ", created='" + dateFormat.format(created.getTime()) + '\''
                + ", postText='" + postText + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(name, post.name)
                && Objects.equals(link, post.link)
                && Objects.equals(created, post.created)
                && Objects.equals(postText, post.postText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, link, created, postText);
    }
}
