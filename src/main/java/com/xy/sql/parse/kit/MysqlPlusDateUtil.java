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
	 * 取得差异天数
	 * 
	 * @param beginDate yyyy-MM-dd
	 * @param endDate   yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static long getDiffDay(String beginDate, String endDate) throws Exception {
		return getDiffDay(parse(beginDate), parse(endDate));
	}

	/**
	 * 取得差异天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static long getDiffDay(Date beginDate, Date endDate) throws Exception {
		Calendar bCal = Calendar.getInstance();
		bCal.setTime(beginDate);
		Calendar eCal = Calendar.getInstance();
		eCal.setTime(endDate);
		return ((eCal.getTimeInMillis() - bCal.getTimeInMillis()) / (1000 * 60 * 60 * 24)) + 1;
	}

	/**
	 * 时间字符串转换为默认： yyyy-MM-dd
	 * 
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
	 * 
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
	 * 转换 yyyy-MM 到 yyyy年MM月 或者 yyyy-MM-dd 到 yyyy年MM月dd日
	 * 
	 * @param date
	 * @param srcPattern
	 * @return
	 * @throws ParseException
	 */
	public static String change(String date, String srcPattern, String tarPattern) throws ParseException {
		SimpleDateFormat f0 = new SimpleDateFormat(srcPattern);
		SimpleDateFormat f1 = new SimpleDateFormat(tarPattern);
		return f1.format(f0.parse(date));
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

	/**
	 * 开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date begin(Date date) {
		try {
			if (date == null) {
				return date;
			}
			String day = format(date);
			return parse(day + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date end(Date date) {
		try {
			if (date == null) {
				return date;
			}
			String day = format(date);
			return parse(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			return date;
		}
	}

	/**
	 * 月份相加
	 * 
	 * @param currentDate yyyy-MM
	 * @param months
	 * @return
	 * @throws Exception
	 */
	public static String addMonth(String currentDate, int months) throws Exception {
		if (currentDate == null || currentDate.trim().length() < 7) {
			throw new Exception("参数不合法");
		}
		String inDate = currentDate;
		if (currentDate.length() > 7) {
			inDate = currentDate.substring(0, 7);
		}
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		return monthFormat.format(addMonth(monthFormat.parse(inDate), months));
	}

	/**
	 * 月份相加
	 * 
	 * @param currentDate
	 * @param months
	 * @return
	 * @throws Exception
	 */
	public static Date addMonth(Date currentDate, int months) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}

	/**
	 * 月份相加
	 *
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public static Date monthBegin(Date currentDate) throws Exception {
		// 获取前月的第一天
		Calendar cale = Calendar.getInstance();
		cale.setTime(currentDate);
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		cale.set(Calendar.HOUR_OF_DAY,0);
		cale.set(Calendar.MINUTE,0);
		cale.set(Calendar.SECOND,0);
		return cale.getTime();
//		// 获取前月的最后一天
//		cale = Calendar.getInstance();
//		cale.add(Calendar.MONTH, 1);
//		cale.set(Calendar.DAY_OF_MONTH, 0);
//		lastday = format.format(cale.getTime());
//		System.out.println("本月第一天和最后一天分别是 ： " + firstday + " and " + lastday);
//
//————————————————
//		版权声明：本文为CSDN博主「sunhuwh」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//		原文链接：https://blog.csdn.net/sunhuwh/article/details/39161323
	}

	/**
	 * 加上指定天
	 * 
	 * @param currentDate yyyy-MM-dd
	 * @param days        天数
	 * @return
	 * @throws Exception
	 */
	public static String addDay(String currentDate, int days) throws Exception {
		return format(addDay(parse(currentDate), days));
	}

	/**
	 * 加上指定天
	 * 
	 * @param currentDate
	 * @param days        天数
	 * @return
	 * @throws Exception
	 */
	public static Date addDay(Date currentDate, int days) throws Exception {
		if (days == 0) {
			return currentDate;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 取得天（1,2,3）标示
	 * 
	 * @param currentDate yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static String getDay(String currentDate) throws Exception {
		return getDay(parse(currentDate));
	}

	/**
	 * 取得天（1,2,3）标示
	 * 
	 * @param currentDate
	 * @return
	 * @throws Exception
	 */
	public static String getDay(Date currentDate) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 取得当前年
	 * 
	 * @param date
	 * @return yyyy
	 * @throws Exception
	 */
	public static String getNowYear(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		return dateFormat.format(date);
	}

	/**
	 * 取得当前月
	 * 
	 * @param date
	 * @return yyyy-MM
	 * @throws Exception
	 */
	public static String getNowMonth(Date date) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		return dateFormat.format(date);
	}


}
