package ru.job4j.grabber.quartz.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ForumTable {
    private String url;
    private int size;
    private Elements unparsedRows;

    public ForumTable(String url) {
        this.url = url;
        init();
    }

    private void init() {
        try {
            Document doc = Jsoup.connect(url).get();
            unparsedRows = doc.select(".forumTable").select("tr");
            size = unparsedRows.size() - 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Post getRow(int i) {
        if (i > size) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        if (i == 0) {
            throw new IllegalArgumentException("Row can not be smaller than 0");
        }
        Elements td = unparsedRows.get(i).select("td");
        return new Post(new PostTopic(td.get(1)), new PostDate(td.get(5)));
    }

    public Post[] rows() {
        Post[] posts = new Post[size];
        for (int i = 0; i < size; i++) {
            Elements td = unparsedRows.get(i + 1).select("td");
            posts[i] = new Post(new PostTopic(td.get(1)), new PostDate(td.get(5)));
        }
        return posts;
    }

}
