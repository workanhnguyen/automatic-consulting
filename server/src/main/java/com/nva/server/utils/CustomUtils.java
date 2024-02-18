package com.nva.server.utils;

import org.reflections.Reflections;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static String encodeInputStreamToBase64Binary(InputStream fileData) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileData.read(buf)) != -1) {
            byteArrayOutputStream.write(buf, 0, bytesRead);
        }
        byte[] fileBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(fileBytes);
    }
}
