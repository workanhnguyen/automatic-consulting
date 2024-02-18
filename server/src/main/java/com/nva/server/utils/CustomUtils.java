package com.nva.server.utils;

import org.reflections.Reflections;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CustomUtils {
    public static String convertMillisecondsToDate(long milliseconds, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(milliseconds);

        return sdf.format(date);
    }

    public static <T> List<String> getSubclassNamesOf(Class<T> parentClass) {
        List<String> subclasses = new ArrayList<>();
        Reflections reflections = new Reflections(parentClass.getPackage().getName());
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(parentClass);
        for (Class<? extends T> subType : subTypes) {
            subclasses.add(subType.getSimpleName());
        }
        return subclasses;
    }
}
