package html;

import quartz.model.ForumTable;
import quartz.model.Post;

public class SqlRuParse {
    public static void main(String[] args) {
        int nrOfPagesToParse = 5;
        for (int pageNr = 1; pageNr < nrOfPagesToParse + 1; pageNr++) {
            ForumTable forumTable = new ForumTable(String.format("https://www.sql.ru/forum/job-offers/%s", pageNr));
            for (Post post : forumTable.rows()) {
                System.out.println(post.link());
                System.out.println(post.text());
                System.out.println(String.format("%s %s %s", post.name(), post.created(), System.lineSeparator()));
            }
        }
    }
}