package quartz.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class PostTopic {
    private final Element topic;
    private String postText;
    private final String href;
    private final String name;

    public PostTopic(Element topic) {
        this.topic = topic;
        href = topic.select("a").first().absUrl("href");
        name = topic.text();
        postText = loadPostText();
    }

    public String name() {
        return name;
    }

    public String href() {
        return href;
    }

    public String postText() {
        return postText;
    }

    private String loadPostText() {
        try {
            Document postDetails = Jsoup.connect(this.href).get();
            return postDetails.select(".msgBody").get(1).text();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to get post text");
        }
    }

    @Override
    public String toString() {
        return "PostTopic{" + topic.text() + '}';
    }
}