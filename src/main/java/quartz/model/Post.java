package quartz.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Post {
    private final Element row;
    private PostTopic topic;
    private PostDate date;
    private PostText postText;

    public Post(Element row) {
        this.row = row;
        init();
    }

    public String name() {
        return topic.name();
    }

    public String text() {
        if (postText == null) {
            loadDetails();
        }
        return postText.text();
    }

    public String link() {
        return topic.href();
    }

    public String created() {
        return date.formatDate();
    }

    private void init() {
        Elements td = row.select("td");
        topic = new PostTopic(td.get(1));
        date = new PostDate(td.get(5));
    }

    private void loadDetails() {
        if (postText != null) {
            return;
        }
        try {
            Document postDetails = Jsoup.connect(topic.href()).get();
            postText = new PostText(postDetails.select(".msgBody"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%s", 1);
        Document doc = Jsoup.connect(url).get();
        Element row = doc.select(".forumTable").select("tr").get(4);
        Post rows = new Post(row);
        rows.loadDetails();
        System.out.println(rows.text());
    }

    public static class PostTopic {
        private final Element topic;

        public PostTopic(Element topic) {
            this.topic = topic;
        }

        public String name() {
            return topic.text();
        }

        public String href() {
            return topic.select("a").first().absUrl("href");
        }

        @Override
        public String toString() {
            return "RowTopic{" + topic.text() + '}';
        }
    }

    public static class PostText {
        private final Elements postText;

        public PostText(Elements postText) {
            this.postText = postText;
        }

        public String text() {
            return postText.get(1).text();
        }
    }


    public static class PostDate {
        private final String[] months = new String[]{"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
        private final Locale ru = new Locale("ru");

        private final Element date;

        public PostDate(Element date) {
            this.date = date;
        }

        public String text() {
            return date.text();
        }

        @Override
        public String toString() {
            return "RowDate{" + date.text() + '}';
        }

        public Calendar calendarDate() {
            return stringToCalendar(date.text());
        }

        private Calendar stringToCalendar(String dateString) {
            Calendar calendar = Calendar.getInstance();
            Date date;
            DateFormatSymbols symbols = DateFormatSymbols.getInstance(ru);
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yy, HH:mm", ru);
            symbols.setMonths(months);
            format.setDateFormatSymbols(symbols);

            if (dateString.contains("сегодня")) {
                date = dateWithDelay(dateString, 0);
            } else if (dateString.contains("вчера")) {
                date = dateWithDelay(dateString, -1);
            } else {
                try {
                    date = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("Cannot pares date: %s", dateString));
                }
            }
            calendar.setTime(date);
            return calendar;
        }

        private Date dateWithDelay(String date, int delay) {
            Calendar cal = Calendar.getInstance();
            String[] time = date.replaceAll("[А-Яа-я ,]", "").split(":");
            cal.add(Calendar.DATE, delay);
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
            return cal.getTime();
        }

        public String formatDate(String format) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, ru);
            return dateFormat.format(calendarDate().getTime());
        }

        public String formatDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", ru);
            return dateFormat.format(calendarDate().getTime());
        }
    }
}
