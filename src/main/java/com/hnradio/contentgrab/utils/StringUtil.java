package com.hnradio.contentgrab.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String processQuotationMarks(String content) {
        String regex = "\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        String reCT = content;

        while (matcher.find()) {
            String itemMatch = "“" + matcher.group(1) + "”";
            reCT = reCT.replace("\"" + matcher.group(1) + "\"", itemMatch);
        }

        return reCT;
    }
}
