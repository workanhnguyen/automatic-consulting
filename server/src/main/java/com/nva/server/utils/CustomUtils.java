package com.nva.server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomUtils {
    public static String convertMillisecondsToDate(long milliseconds, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(milliseconds);

        return sdf.format(date);
    }
}
