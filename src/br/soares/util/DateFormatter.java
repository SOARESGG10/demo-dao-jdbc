package br.soares.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static String format(Date date) {
        return simpleDateFormat.format(date);
    }

    public static Date parse(String date) throws ParseException {return simpleDateFormat.parse(date);}

}
