package quartz.model;

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
        return new Post(unparsedRows.get(i));
    }

    public Post[] rows() {
        Post[] posts = new Post[size];
        for (int i = 0; i < size; i++) {
            posts[i] = new Post(unparsedRows.get(i + 1));
        }
        return posts;
    }

    public int getSize() {
        return size;
    }

    public static void main(String[] args) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%s", 1);
        Document doc = Jsoup.connect(url).get();
        ForumTable forumTable = new ForumTable(url);
        System.out.println(forumTable.getRow(2));
        //System.out.println(post);
    }
}
