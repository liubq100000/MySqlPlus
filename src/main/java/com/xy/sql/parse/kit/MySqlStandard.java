package com.xy.sql.parse.kit;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.node.*;
import com.xy.sql.parse.vo.SqlVO;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 标准钉钉
 */
public class MySqlStandard {
    //支持得所有TAG
    public final static List<MySqlPlusNode> tagList = new ArrayList<MySqlPlusNode>();

    //支持得所有TAG 初始化
    static {
        tagList.add(new MyIfNode());
        tagList.add(new MyWhereNode());
        tagList.add(new MyMapperNode());
        tagList.add(new MySelectNode());
        tagList.add(new MyInsertNode());
        tagList.add(new MyUpdateNode());
        tagList.add(new MyDeleteNode());
        tagList.add(new MyIncludeNode());
        tagList.add(new MySqlNode());
        tagList.add(new MyForeachNode());
        tagList.add(new MyModelNode());
        tagList.add(new MyModelItemNode());
    }

    //最大长度
    public static int MAX_TAG_LEN = 25;
    //结束字符
    public static char XML_END_CHAR = '>';
    //开始字符
    public static char XML_START_CHAR = '<';


    /**
     * 标准化SQL
     *
     * @param inIql
     * @return
     * @throws Exception
     */
    public static String stdSqlAction(String inIql) throws Exception {
        if (inIql.indexOf("/*") > 0 || inIql.indexOf("--") > 0) {
            throw new Exception("sql xml 包含注解信息，目前不支持");
        }
        String sql = inIql + "  ";
        //处理特殊字符
        sql = replaceBr(sql);
        //处理所有标签
        for (MySqlPlusNode entry : MySqlStandard.tagList) {
            sql = replace(sql, entry.getTag());
        }
        int index = sql.indexOf("<mapper");
        return sql.substring(index).trim();
    }


    /**
     * 删除换行等无用特殊字符
     *
     * @param sql
     * @return
     */
    private static String replaceBr(String sql) {
        String nowSql = sql;
        nowSql = nowSql.replaceAll("(\\r\\n|\\n|\\t|\\n\\r)", " ");
        return nowSql;
    }

    /**
     * 替换指定TAG，标准化
     *
     * @param sql
     * @param tag
     * @return
     */
    private static String replace(String sql, String tag) {
        String nowSql = sql;
        nowSql = nowSql.replaceAll("(?i)<\\s*" + tag + "\\s", "<" + tag + " ");
        nowSql = nowSql.replaceAll("(?i)<\\s*/\\s*" + tag + "\\s*>", "</" + tag + "> ");
        nowSql = nowSql.replaceAll("(?i)<\\s*" + tag + ">", "<" + tag + "> ");
        return nowSql;
    }

    //正则
    private static Pattern andPattern = Pattern.compile(" and ", Pattern.CASE_INSENSITIVE);
    private static Pattern orPattern = Pattern.compile(" or ", Pattern.CASE_INSENSITIVE);
    private static Pattern nullPattern = Pattern.compile("null", Pattern.CASE_INSENSITIVE);
    private static Pattern gtPattern = Pattern.compile("&gt;", Pattern.CASE_INSENSITIVE);
    private static Pattern ltPattern = Pattern.compile("&lt;", Pattern.CASE_INSENSITIVE);
    private static Pattern condPattern = Pattern.compile("\"[^\"]*\"");

    /**
     * aviator表达式执行前处理，替换到特殊字符
     *
     * @param condition
     * @return
     * @description 预处理
     */
    public static String aviatorPreProcess(String condition) {
        String newCond = condition;
        if (newCond.indexOf("and") > 0) {
            newCond = replaceAll3(newCond, andPattern, " && ");
        }
        if (newCond.indexOf("or") > 0) {
            newCond = replaceAll3(newCond, orPattern, " || ");
        }
        if (newCond.indexOf("null") > 0) {
            newCond = replaceAll3(newCond, nullPattern, "nil");
        }
        return sqlPreProcess(newCond);
    }

    /**
     * SQL预处理
     *
     * @param condition
     * @return
     */
    public static String sqlPreProcess(String condition) {
        String newCond = condition;
        if (newCond.indexOf("&gt;") > 0) {
            newCond = replaceAll3(newCond, gtPattern, ">");
        }
        if (newCond.indexOf("&lt;") > 0) {
            newCond = replaceAll3(newCond, ltPattern, "<");
        }
        return newCond;
    }

    /***
     * replaceAll,忽略大小写
     *
     * @param input
     * @param p
     * @param replacement
     * @return
     */
    public static String replaceAll3(String input, Pattern p, String replacement) {
        Matcher m = p.matcher(input);
        String result = m.replaceAll(replacement);
        return result;
    }


    /**
     * 等长替换
     *
     * @param inSql
     */
    public static String isometricReplace(String inSql, char targetChar) {
        if (inSql == null || inSql.length() <= 0) {
            return "";
        }
        String sql = inSql;
        String key;
        int len;
        while (true) {
            Matcher matcher = condPattern.matcher(sql);
            if (matcher.find()) {
                key = matcher.group(0);
                len = key.length();
                StringBuilder tempKey = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    tempKey.append(targetChar);
                }
                sql = matcher.replaceFirst(tempKey.toString());
                continue;
            }
            break;
        }
        return sql;
    }

    //**************************************************************************************************************************

    /**
     * 打印格式化
     *
     * @param sql
     * @return
     */
    public static String format(String sql) {
        String nowSql = sql.trim();
        while (nowSql.indexOf("  ") > 0) {
            nowSql = nowSql.replaceAll("  ", " ");
        }
        nowSql += " ";
        StringBuilder s = new StringBuilder("##########################################################################");

        char c;
        char nextC;
        int padd = 0;
        for (int index = 0; index < nowSql.length(); index++) {
            c = nowSql.charAt(index);
            if (c == '<') {
                nextC = nowSql.charAt(index + 1);
                if (nextC == '/') {
                    padd = padd - 4;
                    s.append("\n").append(append(padd));
                    s.append(c);
                } else if (nextC == '!') {
                    s.append("\n").append(append(padd));
                    s.append(c);
                } else {
                    s.append("\n").append(append(padd));
                    padd = padd + 4;
                    s.append(c);
                }

            } else if (c == '>') {
                s.append(c).append("\n");
            } else {
                s.append(c);
            }

        }
        return s.toString();
    }

    /**
     * 添加空格
     *
     * @param len
     * @return
     */
    private static String append(int len) {
        StringBuilder s = new StringBuilder();
        for (int index = 0; index < len; index++) {
            s.append("*");
        }
        return s.toString();
    }

    /**
     * 参数打印
     *
     * @param vo
     * @return
     */
    public static void printSql(SqlVO vo) {
        System.out.println(vo.getSql().trim());
        Object value;
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sqlPara = new StringBuilder();
        for (int i = 0; i < vo.getVoList().size(); i++) {
            value = vo.getVoList().get(i);
            if (value != null && value instanceof Date) {
                sqlPara.append(f1.format((Date) value)).append(" ");
            } else {
                sqlPara.append(value).append(" ");
            }
        }
        System.out.println(sqlPara.toString());
    }

}
