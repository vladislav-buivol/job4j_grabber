package quartz.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Post {
    private String url;
    private int size;
    private Elements unparsedRows;

    public Post(String url) {
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

    public Row getRow(int i) {
        if (i > size) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
        if (i == 0) {
            throw new IllegalArgumentException("Row can not be smaller than 0");
        }
        return new Row(unparsedRows.get(i));
    }

    public Row[] rows() {
        Row[] rows = new Row[size];
        for (int i = 0; i < size; i++) {
            rows[i] = new Row(unparsedRows.get(i + 1));
        }
        return rows;
    }

    public int getSize() {
        return size;
    }

    public static void main(String[] args) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%s", 1);
        Document doc = Jsoup.connect(url).get();
        Post post = new Post(url);
        System.out.println(post.getRow(2));
        //System.out.println(post);
    }
}
