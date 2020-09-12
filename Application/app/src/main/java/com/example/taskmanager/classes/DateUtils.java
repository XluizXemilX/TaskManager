package com.example.taskmanager.classes;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static String toLongFormattedDate(Date date) {
        return formatDate(date, "EEE, MMM d, yyyy");
    }

    public static String toHourFormat(Date date) {
        return formatDate(date, "h:mm a");
    }

    public static Date getDateFromIso8601String(String date) {
        if (date == null || date.isEmpty()){
            return DateUtils.currentDate();
        }
        return getDateFromString(date, ISO_DATE_FORMAT);
    }

    public static String getIso8601StringFromDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date getDateFromString(String fecha, String format) {
        if (!TextUtils.isEmpty(fecha) && !TextUtils.isEmpty(format)) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
                return simpleDateFormat.parse(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date toDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getCurrentDateWithoutTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Calendar createDefaultCalendar() {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    }

    public static String formatDate(Date date) {
        return formatDate(date, "MM/dd/yyyy");
    }

    public static Date currentDate() {
        return createDefaultCalendar().getTime();
    }

    public static boolean isAPastDate(Date date) {
        return date.compareTo(currentDate()) <= 0;
    }

    private static boolean isADateRange(Date startDate, Date endDate) {
        return startDate.compareTo(endDate) < 0;
    }

    public static boolean isNotADateRange(Date startDate, Date endDate) {
        return !isADateRange(startDate, endDate);
    }

    public static String formatDate(Date date, String format) {
        if(date == null) {
            return  "invalid date!";
        }
        if(TextUtils.isEmpty(format)) {
            return  getIso8601StringFromDate(date);
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static Date currentTime() {
        return createDefaultCalendar().getTime();
    }

    public static boolean isDateInRange(Date date, Date startDate, Date endDate) {
        return date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0;
    }

    public static boolean isThisDateWithin3MonthsRange(String dateToValidate,
                                                String dateFromat) {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {

            // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

            // current date after 3 months
            Calendar currentDateAfter3Months = Calendar.getInstance();
            currentDateAfter3Months.add(Calendar.MONTH, 3);

            // current date before 3 months
            Calendar currentDateBefore3Months = Calendar.getInstance();
            currentDateBefore3Months.add(Calendar.MONTH, -3);

            /*************** verbose ***********************/
            System.out.println("\n\ncurrentDate : "
                    + Calendar.getInstance().getTime());
            System.out.println("currentDateAfter3Months : "
                    + currentDateAfter3Months.getTime());
            System.out.println("currentDateBefore3Months : "
                    + currentDateBefore3Months.getTime());
            System.out.println("dateToValidate : " + dateToValidate);
            /************************************************/

            if (date.before(currentDateAfter3Months.getTime())
                    && date.after(currentDateBefore3Months.getTime())) {

                //ok everything is fine, date in range
                return true;

            } else {

                return false;

            }

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

    }

    public static Date addMonthsToDate(Date date, int months) {
        Calendar c =  Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    public static long differenceInDays(Date beginningDate, Date endingDate) {

        if (beginningDate == null || endingDate == null) {
            return 0;
        }
        long startTime = beginningDate.getTime();
        long endTime = endingDate.getTime();
        long diffTime = endTime - startTime;
        return diffTime / (1000 * 60 * 60 * 24);
    }
}
