package com.xy.sql.build.kit;

public class NameUtil {
    public static String className(String value) {
        String[] arr = value.split("_");
        StringBuilder re = new StringBuilder();
        for (String a : arr) {
            if (a.length() == 1) {
                String ta = a;
                a += ta;
                a += ta;
            }
            re.append(String.valueOf(a.charAt(0)).toUpperCase()).append(a.substring(1).toLowerCase());
        }
        return re.toString();
    }

    public static String methodName(String value) {
        String[] arr = value.split("_");
        StringBuilder re = new StringBuilder();
        for (String a : arr) {
            re.append(String.valueOf(a.charAt(0)).toUpperCase()).append(a.substring(1).toLowerCase());
        }
        return re.toString();
    }

    public static String fieldName(String value) {
        String[] arr = value.split("_");
        StringBuilder re = new StringBuilder();
        boolean first = true;
        for (String a : arr) {
            if (first) {
                first = false;
                re.append(String.valueOf(a.charAt(0)).toLowerCase()).append(a.substring(1).toLowerCase());
            } else {
                re.append(String.valueOf(a.charAt(0)).toUpperCase()).append(a.substring(1).toLowerCase());
            }
        }
        return re.toString();
    }

}
