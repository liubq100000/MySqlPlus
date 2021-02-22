package com.xy.sql.parse.kit;

import com.googlecode.aviator.AviatorEvaluator;
import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.fragment.FieldFragment;
import com.xy.sql.parse.fragment.IFragment;
import com.xy.sql.parse.fragment.StringFragment;
import com.xy.sql.parse.node.MyModelItemNode;
import com.xy.sql.parse.node.MyStringNode;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.ExpVO;
import com.xy.sql.parse.vo.SqlExePara;
import com.xy.sql.parse.vo.SqlVO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 核心工具类
 */
public class MySqlPlusKit {


    /**
     * 分类成不通类型的节点
     *
     * @param oriSql
     * @param begin
     * @return
     * @throws Exception
     */
    private static MySqlPlusNode dispatch(final String oriSql, final int begin) throws Exception {
        MySqlPlusNode node = null;
        //取得位置
        int nextPosition = oriSql.indexOf(MySqlStandard.XML_START_CHAR, begin);
        if (nextPosition < 0) {
            return new MyStringNode();
        }
        //取得标签前缀
        String cond;
        if (nextPosition == begin) {
            if (begin + MySqlStandard.MAX_TAG_LEN < oriSql.length()) {
                cond = oriSql.substring(begin, begin + MySqlStandard.MAX_TAG_LEN);
            } else {
                cond = oriSql.substring(begin);
            }
        } else {
            cond = oriSql.substring(begin, nextPosition).trim();
            //去掉空格后仍有字符，则式字符传
            if (cond.length() > 0) {
                return new MyStringNode();
            } else {
                if (nextPosition + MySqlStandard.MAX_TAG_LEN < oriSql.length()) {
                    cond = oriSql.substring(nextPosition, nextPosition + MySqlStandard.MAX_TAG_LEN);
                } else {
                    cond = oriSql.substring(nextPosition);
                }
            }
        }
        for (MySqlPlusNode entry : MySqlStandard.tagList) {
            if (cond.startsWith(MySqlStandard.XML_START_CHAR + entry.getTag())) {
                node = entry;
                break;
            }
        }
        if (node == null) {
            return new MyStringNode();
        }
        return node.clone();
    }

    /**
     * 解析整个SQL
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public static List<MySqlPlusNode> parse(final String sql) throws Exception {
        List<MySqlPlusNode> list = new ArrayList<MySqlPlusNode>();
        if (sql == null || sql.length() <= 0) {
            return list;
        }
        int len = sql.length();
        int nowIndex = 0;
        MySqlPlusNode node;
        while (nowIndex < len) {
            node = MySqlPlusKit.dispatch(sql, nowIndex);
            if (node != null) {
                nowIndex = node.action(sql, nowIndex);
                list.add(node);
            }
        }
        return list;

    }

    //表达式正则
    private static Pattern fragmentPattern = Pattern.compile("((\\$\\{[\\s\\w,=\"\'-]*\\})|(\\#\\{[\\s\\w,=\"\'-]*\\}))");

    /**
     * 解析SQL表达式
     *
     * @param sql
     * @return
     * @description 分析片段
     */
    public static List<IFragment> processFragment(String sql) {
        String newSql = sql;

        Matcher m = fragmentPattern.matcher(newSql);
        List<IFragment> voList = new ArrayList<IFragment>();
        String preStr;
        String fieldStr;
        int begin;
        int end;
        char type;
        while (m.find()) {
            begin = m.start();
            if (begin > 0) {
                preStr = newSql.substring(0, begin);
                if (preStr != null && preStr.trim().length() > 0) {
                    voList.add(new StringFragment(preStr.trim()));
                }
            }
            fieldStr = m.group().trim();
            type = fieldStr.charAt(0);
            voList.add(new FieldFragment(m.group(), '$' == type));
            end = m.end();
            if (end < newSql.length()) {
                newSql = newSql.substring(end);
                m = fragmentPattern.matcher(newSql);
                continue;
            } else {
                newSql = "";
            }
            break;
        }
        if (newSql.trim().length() > 0) {
            voList.add(new StringFragment(newSql));
        }
        return voList;
    }


    /**
     * 取得所有属性
     *
     * @param cond
     */
    public static Map<String, String> parseProperty(String cond) {
        String oriCond = cond;
        char[] newCondList = MySqlStandard.isometricReplace(oriCond, MySqlStandard.XML_END_CHAR).toCharArray();
        int begin = 0;
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < newCondList.length; i++) {
            if (newCondList[i] == MySqlStandard.XML_END_CHAR) {
                if (i == newCondList.length - 1 || newCondList[i + 1] != MySqlStandard.XML_END_CHAR) {
                    itemList.add(oriCond.substring(begin, i + 1));
                    begin = i + 1;
                }
            }
        }
        Map<String, String> resMap = new HashMap<>();
        String itemCond;
        int index;
        String key;
        String value;
        for (String item : itemList) {
            itemCond = item.trim();
            index = itemCond.indexOf("=");
            key = itemCond.substring(0, index).trim();
            value = itemCond.substring(index + 1).trim();
            value = value.substring(1, value.length() - 1);
            resMap.put(key, value);
        }
        return resMap;
    }

    /**
     * 执行表达式
     *
     * @param condition
     * @return boolean
     * @description 条件判断
     */
    public static boolean predicate(String condition, Map<String, Object> paramters) {
        if (condition == null || condition.trim().length() == 0) {
            return true;
        }
        if (paramters == null) {
            paramters = new HashMap<String, Object>();
        }
        // 预处理
        String newCond = MySqlStandard.aviatorPreProcess(condition);

        // 执行表达式
        Boolean result2 = (Boolean) AviatorEvaluator.execute(newCond, paramters);
        return result2;
    }

    /**
     * 转换为SQL语句，绑定参数
     *
     * @param context
     * @param sqlId
     * @param inParamters
     * @return SqlVO
     * @throws Exception
     */
    public static SqlVO bound(ContextVO context, String sqlId, Map<String, Object> inParamters) throws Exception {
        //取得指定节点
        MySqlPlusNode node = context.getSqlFragmentById(sqlId);
        if (node == null) {
            return null;
        }
        List<MySqlPlusNode> nodeList = node.getChildList();
        if (nodeList == null || nodeList.size() == 0) {
            return null;
        }
        //参数转变,类型转换
        Map<String, Object> param = convert(context, inParamters);

        //继续绑定
        List<SqlExePara> list = new ArrayList<>();
        List<SqlExePara> subList;
        for (MySqlPlusNode temp : nodeList) {
            subList = temp.boundSql(context, param);
            if (subList != null && subList.size() > 0) {
                list.addAll(subList);
            }
        }

        StringBuilder sql = new StringBuilder();
        String fragment;
        List<Object> paramList = new ArrayList<Object>();
        for (SqlExePara fvo : list) {
            if (fvo == null) {
                continue;
            }
            fragment = fvo.getSqlFragment();
            sql.append(fragment).append(" ");
            if (fvo.isContainNull()) {
                paramList.add(fvo.getValue());
            } else {
                if (fvo.getValue() != null) {
                    paramList.add(fvo.getValue());
                }
            }

        }
        return new SqlVO(sql.toString(), paramList);
    }

    /**
     * 根据模型转换参数
     *
     * @param context
     * @param inParamters
     * @return
     * @throws Exception
     */
    private static Map<String, Object> convert(ContextVO context, Map<String, Object> inParamters) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        Map<String, Map<String, String>> itemMap = context.getItemMap();
        Map<String, String> nowProp;
        String type;
        for (Map.Entry<String, Object> entry : inParamters.entrySet()) {
            Object value = entry.getValue();
            resMap.put(entry.getKey(), value);
            if (itemMap.containsKey(entry.getKey()) && entry.getValue() != null) {
                nowProp = itemMap.get(entry.getKey());
                type = nowProp.get(MyModelItemNode.ATT_TYPE);
                //类型转换
                resMap.put(entry.getKey(), convertValue(type, value));
            }
        }
        return resMap;
    }

    /**
     * 值转换
     *
     * @param inType
     * @param value
     * @return
     * @throws Exception
     */
    public static Object convertValue(String inType, Object value) {
        if (inType == null) {
            return value;
        }
        if (value == null) {
            return null;
        }
        String type = inType.toUpperCase();
        String tempValue = value.toString().trim();
        if ("DOUBLE".equalsIgnoreCase(type) || "FLOAT".equalsIgnoreCase(type)) {
            if (value instanceof Double || value instanceof Float) {
                return value;
            }
            return Double.valueOf(tempValue);
        } else if ("DECIMAL".equalsIgnoreCase(type)) {
            if (value instanceof BigDecimal) {
                return value;
            }
            return BigDecimal.valueOf(Double.valueOf(tempValue));
        } else if ("TINYINT".equalsIgnoreCase(type) || "SMALLINT".equalsIgnoreCase(type) || "INT".equalsIgnoreCase(type)) {
            if (value instanceof Integer) {
                return value;
            }
            return Integer.valueOf(tempValue);
        } else if ("LONG".equalsIgnoreCase(type) || "BIGINT".equalsIgnoreCase(type)) {
            if (value instanceof Long) {
                return value;
            }
            return Long.valueOf(tempValue);
        } else if ("DATE".equalsIgnoreCase(type) || "DATETIME".equalsIgnoreCase(type) || "TIMESTAMP".equalsIgnoreCase(type)) {
            if (value instanceof Date) {
                return value;
            }
            return MysqlPlusDateUtil.extractDateTime(tempValue);
        } else {
            return value;
        }
    }

    /**
     * 值转换
     *
     * @param format
     * @param value
     * @return
     * @throws Exception
     */
    public static String formatValue(String format, Object value) {
        if (value == null) {
            return null;
        }
        if (format == null) {
            return value.toString();
        }

        if (value instanceof Date) {
            return MysqlPlusDateUtil.format((Date) value, format);
        } else if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
            Double d = Double.valueOf(value.toString());
            int scale = 0;
            int len = format.indexOf(".");
            if (len < 0) {
                scale = 0;
            } else {
                scale = format.length() - (scale + 1);
            }
            return BigDecimal.valueOf(d).setScale(scale, RoundingMode.HALF_UP).toString();
        }
        return value.toString();
    }


    /**
     * 解析节点内容
     *
     * @param tag
     * @param inSql
     * @param beginIndex
     * @return
     */
    public static ExpVO parseNodeText(String tag, String inSql, int beginIndex) {
        String sql = inSql;
        String beginKey = "<" + tag;
        int tagBeginIndex = sql.indexOf(beginKey, beginIndex);
        int tagBeginIndex2 = sql.indexOf(">", tagBeginIndex);
        String cond = "";
        if (tagBeginIndex + beginKey.length() + 1 < tagBeginIndex2) {
            cond = sql.substring(tagBeginIndex + beginKey.length() + 1, tagBeginIndex2);
        }

        String endKey = "</" + tag + ">";
        int cout = 0;
        int nowBegin = tagBeginIndex2;
        int tagEndIndex;
        int tempTagBeginIndex;
        while (true) {
            cout++;
            if (cout > 100) {
                throw new RuntimeException("解析异常！");
            }
            tagEndIndex = sql.indexOf(endKey, nowBegin);
            tempTagBeginIndex = sql.indexOf(beginKey, nowBegin);
            if (tempTagBeginIndex < 0 || tempTagBeginIndex > tagEndIndex) {
                break;
            }
            nowBegin = tagEndIndex + endKey.length();
        }
        String text = sql.substring(tagBeginIndex2 + 1, tagEndIndex).trim();
        return new ExpVO(cond, text, tagEndIndex + endKey.length());
    }
}
