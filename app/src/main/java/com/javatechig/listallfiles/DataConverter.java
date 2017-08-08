package com.javatechig.listallfiles;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chiaying.wu on 2017/8/2.
 */

public class DataConverter {

    public static int[] parseDateText(String strDate) {
        int[] iArrDate = new int[3];
        String[] strArrDate = strDate.split("/");
        for (int i = 0; i < 3; i++) {
            iArrDate[i] = Integer.parseInt(strArrDate[i]);
        }
        return iArrDate;
    }

    public static Date convertToDate(int iYear, int iMonth, int iDay, Boolean isEndDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, iYear);
        calendar.set(Calendar.MONTH, iMonth - 1);
        if (isEndDate) iDay++;
        calendar.set(Calendar.DAY_OF_MONTH, iDay);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);// for 0 min
        calendar.set(Calendar.SECOND, 0);// for 0 sec
        Date date = new Date(calendar.getTimeInMillis());

        return date;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }
}
