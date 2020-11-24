package html;

import quartz.model.Post;
import quartz.model.Row;

public class SqlRuParse {
    public static void main(String[] args) {
        int nrOfPagesToParse = 5;
        for (int pageNr = 1; pageNr < nrOfPagesToParse + 1; pageNr++) {
            Post post = new Post(String.format("https://www.sql.ru/forum/job-offers/%s", pageNr));
            for (Row row : post.rows()) {
                System.out.println(row.getTopic().href());
                System.out.println(String.format("%s %s %s", row.getTopic().text(), row.getDate().formatDate(), System.lineSeparator()));
            }
        }
    }
}