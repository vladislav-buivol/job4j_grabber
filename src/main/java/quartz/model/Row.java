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

public class Row {
    private final Element row;
    private RowIcon icon;
    private RowTopic topic;
    private RowAuthor author;
    private RowAnswer answer;
    private RowView view;
    private RowDate date;

    public Row(Element row) {
        this.row = row;
        init();
    }

    private void init() {
        Elements td = row.select("td");
        icon = new RowIcon(td.get(0));
        topic = new RowTopic(td.get(1));
        author = new RowAuthor(td.get(2));
        answer = new RowAnswer(td.get(3));
        view = new RowView(td.get(4));
        date = new RowDate(td.get(5));
    }

    public Element getRow() {
        return row;
    }

    public RowIcon getIcon() {
        return icon;
    }

    public RowTopic getTopic() {
        return topic;
    }

    public RowAuthor getAuthor() {
        return author;
    }

    public RowAnswer getAnswer() {
        return answer;
    }

    public RowView getView() {
        return view;
    }

    public RowDate getDate() {
        return date;
    }

    public static void main(String[] args) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%s", 1);
        Document doc = Jsoup.connect(url).get();
        Element row = doc.select(".forumTable").select("tr").get(4);
        Row rows = new Row(row);
        System.out.println(rows);
    }

    public class RowIcon {
        private final Element icon;

        public RowIcon(Element icon) {
            this.icon = icon;
        }

        public String text() {
            return topic.text();
        }

        public String img() {
            return icon.select("img").first().absUrl("src");
        }

        @Override
        public String toString() {
            return "RowIcon{" + img() + '}';
        }
    }

    public class RowTopic {
        private final Element topic;

        public RowTopic(Element topic) {
            this.topic = topic;
        }

        public String text() {
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

    public class RowAuthor {
        private final Element author;

        public RowAuthor(Element author) {
            this.author = author;
        }

        public String text() {
            return author.text();
        }

        @Override
        public String toString() {
            return "RowAuthor{" + author.text() + '}';
        }
    }

    public class RowAnswer {
        private final Element answer;

        public RowAnswer(Element answer) {
            this.answer = answer;
        }

        public String text() {
            return answer.text();
        }

        @Override
        public String toString() {
            return "RowAnswer{" + answer.text() + '}';
        }
    }

    public class RowView {
        private final Element view;

        public RowView(Element view) {
            this.view = view;
        }

        public String text() {
            return view.text();
        }

        @Override
        public String toString() {
            return "RowView{" + view.text() + '}';
        }
    }

    public class RowDate {
        private final String[] months = new String[]{"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
        private final Locale ru = new Locale("ru");

        private final Element date;

        public RowDate(Element date) {
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
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", ru);
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, ru);
            return dateFormat.format(calendarDate().getTime());
        }

        public String formatDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", ru);
            return dateFormat.format(calendarDate().getTime());
        }
    }
}
