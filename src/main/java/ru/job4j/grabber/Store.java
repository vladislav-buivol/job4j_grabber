package ru.job4j.grabber;

import ru.job4j.grabber.quartz.model.Post;

import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll();

    Post findById(String id);
}