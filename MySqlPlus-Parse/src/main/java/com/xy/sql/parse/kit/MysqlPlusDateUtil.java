package com.xy.sql.parse.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间相关计算
 *
 * @author lc liubq
 * @since 20170711
 */
public class MysqlPlusDateUtil {

    /**
     * 抽取 yyyy-MM-dd HH:mm:ss时间
     *
     * @param text
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String extractToDateTimeStr(String text) {
        Date d = extractToDateTime(text);
        if (d == null) {
            return null;
        }
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f1.format(d);
    }

    /**
     * 抽取 yyyy-MM-dd HH:mm:ss时间
     *
     * @param text
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static Date extractToDateTime(String text) {
        String v = extract(text);
        if (v == null) {
            return null;
        }
        if (v.length() == 10) {
            try {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                return f.parse(v);
            } catch (ParseException e) {
                return null;
            }
        } else if (v.length() == 19) {
            try {
                SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return f1.parse(v);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 抽取
     *
     * @param text
     * @return
     */
    public static String extract(String text) {
        if (text == null) {
            return "";
        }
        String dateStr = text.replaceAll("r?n", " ");
        String value = extractDateTime(dateStr);
        if (value == null) {
            value = extractDate(dateStr);
        }
        value = value == null ? "" : value.trim();
        return value;
    }

    /**
     * 抽取日期
     *
     * @param dateStr
     * @return
     */
    public static String extractDate(String dateStr) {
        try {
            Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|.]\\d{1,2}[-|-|\\/|.]\\d{1,2})", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher matcher = p.matcher(dateStr);
            if (matcher.find() && matcher.groupCount() >= 1) {
                return replace(matcher.group(0));
            } else {
                Pattern p1 = Pattern.compile("(\\d{8})", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
                Matcher matcher1 = p1.matcher(dateStr);
                if (matcher1.find() && matcher1.groupCount() >= 1) {
                    return replace(matcher1.group(0));
                }
                return null;
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 抽取时间
     *
     * @param dateStr
     * @return
     */
    public static String extractDateTime(String dateStr) {
        try {
            Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|.]\\d{1,2}[-|-|\\/|.]\\d{1,2}[\\s]\\d{1,2}[:]\\d{1,2}[:]\\d{1,2})", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher matcher = p.matcher(dateStr);
            if (matcher.find() && matcher.groupCount() >= 1) {
                return replace(matcher.group(0));
            }
            return null;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 替换
     *
     * @param str
     * @return
     */
    private static String replace(String str) {
        if (str != null && str.trim().length() > 0) {
            String inStr = str.trim();
            inStr = inStr.replace("/", "-");
            inStr = inStr.replace(".", "-");
            if (inStr.length() > 10) {
                return inStr;
            } else {
                if (inStr.indexOf("-") > 0) {
                    String[] resStr = inStr.split("-");
                    String y = resStr[0];
                    String m = "00";
                    if (resStr.length > 1) {
                        if (resStr[1].length() < 2) {
                            m = "0" + resStr[1];
                        } else {
                            m = resStr[1];
                        }
                    }
                    String d = "00";
                    if (resStr.length > 2) {
                        if (resStr[2].length() < 2) {
                            d = "0" + resStr[2];
                        } else {
                            d = resStr[2];
                        }
                    }
                    return y + "-" + m + "-" + d;
                }
                if (inStr.length() == 8) {
                    return inStr.substring(0, 4) + "-" + inStr.substring(4, 6) + "-" + inStr.substring(6, 8);
                }
            }
        }
        return "";
    }


    /**
     * 时间字符串转换为默认： yyyy-MM-dd
     * <p>
     * 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException {
        return parse(date);
    }

    /**
     * 时间字符串转换为默认： yyyy-MM-dd
     * <p>
     * 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String date, String pattern) throws ParseException {
        String p = pattern;
        if (p == null || p.trim().length() == 0) {
            p = "yyyy-MM-dd";
        }
        SimpleDateFormat f = new SimpleDateFormat(p);
        return f.parse(date);
    }


    /**
     * 时间格式，默认格式1 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 时间格式，默认格式1 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        String p = pattern;
        if (p == null || p.trim().length() == 0) {
            p = "yyyy-MM-dd";
        }
        SimpleDateFormat f = new SimpleDateFormat(p);
        return f.format(date);
    }

    /**
     * 时间格式，默认格式1 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param c
     * @return
     */
    public static String format(Calendar c) {
        return format(c);
    }

    /**
     * 时间格式，默认格式1 格式1： yyyy-MM-dd 格式2：yyyy-MM-dd HH:mm:ss
     *
     * @param c
     * @param pattern
     * @return
     */
    public static String format(Calendar c, String pattern) {
        String p = pattern;
        if (p == null || p.trim().length() == 0) {
            p = "yyyy-MM-dd";
        }
        SimpleDateFormat f = new SimpleDateFormat(p);
        return f.format(c.getTime());
    }


}
