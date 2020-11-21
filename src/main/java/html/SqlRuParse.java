package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".forumTable").select("tr");
        for (int i = 1; i < row.size(); i++) {
            Element td = row.get(i);
            Element href = td.select(".postslisttopic").get(0).child(0);
            Element date = td.select(".altCol").get(1);
            System.out.println(href.attr("href"));
            System.out.println(String.format("%s, %s", href.text(), date.text()));
            System.out.println();
        }
    }
}