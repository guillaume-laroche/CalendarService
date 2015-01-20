package fr.axin.gservices.util;

import java.lang.String;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


public abstract class DateUtils {
    public static java.util.Date toDate(String dateString,
                                        SimpleDateFormat datePattern) throws java.text.ParseException {
        java.util.Date date = null;
        try {
            date = datePattern.parse(dateString);
        } catch (java.text.ParseException eParse) {
            System.out.println(eParse.getMessage());
        }
        return date;
    }

    public static double getDaysBetweenDates(java.util.Date theEarlierDate, java.util.Date theLaterDate) {
        double result = Double.POSITIVE_INFINITY;
        if (theEarlierDate != null && theLaterDate != null) {
            final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
            Calendar aCal = Calendar.getInstance();
            aCal.setTime(theEarlierDate);
            long aFromOffset = aCal.get(Calendar.DST_OFFSET);
            aCal.setTime(theLaterDate);
            long aToOffset = aCal.get(Calendar.DST_OFFSET);
            long aDayDiffInMili = (theLaterDate.getTime() + aToOffset) - (theEarlierDate.getTime() + aFromOffset);
            result = ((double)aDayDiffInMili / MILLISECONDS_PER_DAY);
        }
        return result;
    }

    public static Date addDaysToDate(java.util.Date dateRef, int nDays) {
        Calendar aCal = Calendar.getInstance();
        aCal.setTime(dateRef);
        aCal.add(Calendar.DATE, nDays);
        return aCal.getTime();
    }

    public static String dateToString(Date date, SimpleDateFormat datePattern) {
        String dateString = null;
        dateString = datePattern.format(date);
        return dateString;
    }

    public static String toGoogleDateString(String date, String hour) {
        return date.substring(6, 10) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2) + "T" + hour + "+01:00";
    }

    public static String getNextJobDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date d = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");

        return sdf.format(d);
    }
}
