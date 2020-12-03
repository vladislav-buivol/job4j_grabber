package ru.job4j.grabber.quartz.model;

import org.jsoup.nodes.Element;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostDate {
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