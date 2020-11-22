package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SqlRuParse {
    private static final String[] MONTHS = new String[]{"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};
    private static final Locale RU = new Locale("ru");

    public static void main(String[] args) throws Exception {
        int nrOfPagesToParse = 5;
        for (int pageNr = 1; pageNr < nrOfPagesToParse + 1; pageNr++) {
            String url = String.format("https://www.sql.ru/forum/job-offers/%s", pageNr);
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".forumTable").select("tr");
            for (int i = 1; i < row.size(); i++) {
                Element td = row.get(i);
                Element href = td.select(".postslisttopic").get(0).child(0);
                Element date = td.select(".altCol").get(1);
                System.out.println(href.attr("href"));
                System.out.println(String.format("%s, %s", href.text(), formatDate(stringToCalendar(date.text()))));
                System.out.println();
            }
        }
    }

    private static Calendar stringToCalendar(String dateString) {
        Calendar calendar = Calendar.getInstance();
        Date date;
        DateFormatSymbols symbols = DateFormatSymbols.getInstance(RU);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy, HH:mm", RU);
        symbols.setMonths(MONTHS);
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

    private static Date dateWithDelay(String date, int delay) {
        Calendar cal = Calendar.getInstance();
        String[] time = date.replaceAll("[А-Яа-я ,]", "").split(":");
        cal.add(Calendar.DATE, delay);
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        return cal.getTime();
    }

    private static String formatDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy, HH:mm", RU);
        return dateFormat.format(calendar.getTime());
    }
}