package org.ieszaidinvergeles.android.webizv.util;

// WIzv

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Util {

    public static long dateToLong(String date) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
            final Date fecha = sdf.parse(date);
            return fecha.getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String formatDate(Date date) {
        return formatStringDate("yyyy-MM-dd_hh-mm-ss", date);
    }

    public static String formatStringDate(String formatStr, Date date) {
        if (date == null) {
            return null;
        }
        return android.text.format.DateFormat.format(formatStr, date).toString();
    }

    public static String getDate(long time) {
        final Calendar calendar = Calendar.getInstance(new Locale("es", "ES"));
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString();
    }

    public static String getDate(String time) {
        return Util.getDate(Util.getLongFromPHPTime(time));
    }

    public static String getJwt(String message, String key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("message", message);
        claims.put("time", System.currentTimeMillis());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return jwt;
    }

    public static long getLongFromPHPTime(String strLong) {
        try {
            return (Long.parseLong(strLong)) * 1_000;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String[] getNewArray(String[] initialArray, String parameter){
        String [] newArray;
        if (initialArray == null) {
            return new String[]{parameter};
        }
        newArray = new String[initialArray.length + 1];
        for (int i = 0; i < initialArray.length; i++) {
            newArray[i] = initialArray[i];
        }
        newArray[initialArray.length] = parameter;
        return newArray;
    }

    public static String getSQLConditions(String initialCondition, String newCondition) {
        return getSQLConditions(initialCondition, newCondition, "and");
    }

    public static String getSQLConditions(String initialCondition, String newCondition, String conector) {
        if(initialCondition == null || initialCondition.trim().length() == 0 ) {
            return newCondition;
        }
        return initialCondition + " " + conector + " " + newCondition;
    }

    public static ArrayList<String> splitMessage(String message) {
        ArrayList<String> splittedMessage = new ArrayList<String>();
        if (message != null) {
            String[] splittedArray = message.split("\\s*;\\s*");
            for (int i = 0; i < splittedArray.length; i++) {
                if (!(splittedArray[i] == null) || !(splittedArray[i].length() == 0)) {
                    splittedMessage.add(splittedArray[i].trim());
                }
            }
        }
        return splittedMessage;
    }

    public static String subString(String s, int length){
        final String value;
        if (s == null || s.length() <= 0) {
            return "";
        } else if (s.length() <= length) {
            return s;
        } else {
            return s.substring(0, length) + " ...";
        }
    }

    public static String weekDayFormat(String date) {
        if(date == null || date.equals("")) {
            return "";
        }
        final Calendar calendar = Calendar.getInstance(new Locale("es", "ES"));
        calendar.setTimeInMillis(Util.getLongFromPHPTime(date));
        return DateFormat.format("EEEE, dd MMM HH:mm", calendar).toString();
    }
}