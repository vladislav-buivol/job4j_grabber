package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import ru.job4j.grabber.quartz.model.Post;
import ru.job4j.grabber.quartz.model.PostDate;
import ru.job4j.grabber.quartz.model.PostTopic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    public static void main(String[] args) {
        SqlRuParse parse = new SqlRuParse();
        String link = "https://www.sql.ru/forum/job-offers";
        parse.list(link)
                .forEach(System.out::println);
    }

    @Override
    public List<Post> list(String link) {
        Document doc = connect(link);
        Elements unparsedRows = doc.select(".forumTable").select("tr");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < unparsedRows.size() - 1; i++) {
            posts.add(detail(unparsedRows.get(i + 1).toString()));
        }
        return posts;
    }

    @Override
    public Post detail(String post) {
        Element row = Jsoup.parse(post, "", Parser.xmlParser());
        return new Post(new PostTopic(row.select("td").get(1)), new PostDate(row.select("td").get(5)));
    }

    private Document connect(String link) {
        try {
            return Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(link);
        }
    }
}