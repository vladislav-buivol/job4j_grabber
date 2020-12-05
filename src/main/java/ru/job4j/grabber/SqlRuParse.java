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
import java.util.Calendar;
import java.util.List;

public class SqlRuParse implements Parse {
    public static void main(String[] args) {
        SqlRuParse parse = new SqlRuParse();
        String link = "https://www.sql.ru/forum/job-offers";
        parse.detail(link);
        parse.list(link)
                .forEach(System.out::println);
    }

    @Override
    public List<Post> list(String link) {
        Document doc = connect(link);
        Elements unparsedRows = doc.select(".forumTable").select("tr");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < unparsedRows.size() - 1; i++) {
            Element row = unparsedRows.get(i + 1);
            PostTopic topic = new PostTopic(row.select("td").get(1));
            PostDate date = new PostDate(row.select("td").get(5));
            posts.add(new Post(topic, date));
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        try {
            Document postDetails = Jsoup.connect(link).get();
            String postHeader = postDetails.select(".messageHeader").get(0).text();
            String time = postDetails.select(".msgFooter").get(0).text().split("\\[")[0];
            Calendar date = new PostDate().calendarDate(time);
            String postText = postDetails.select(".msgBody").get(1).text();
            return new Post(postHeader, link, date, postText);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to get post text");
        }

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