package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.kit.MySqlPlusKit;

import java.util.HashMap;
import java.util.Map;

public class MyModelItemNode extends MySqlPlusNode {

    public static final String ATT_NAME = "name";

    public static final String ATT_TYPE = "type";

    public static final String ATT_FORMAT = "format";

    @Override
    public String getTag() {
        return "item";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyModelItemNode();
    }

    public int action(String inSql, int beginIndex) throws Exception {
        String beginKey = "<" + getTag();
        String endKey = "/>";
        int tagBeginPosition = inSql.indexOf(beginKey, beginIndex);
        int tagEndPosition = inSql.indexOf(endKey, tagBeginPosition + beginKey.length());

        String tempCond = inSql.substring(tagBeginPosition + beginKey.length(), tagEndPosition);
        if (tempCond.indexOf("<") > 0 || tempCond.indexOf(">") > 0) {
            throw new Exception("item 标签格式不合法，格式要求：<item column=\"id\" property=\"id\" type=\"bigint\" />");
        }
        Map<String, String> resMap = MySqlPlusKit.parseProperty(tempCond);
        super.setPropertyMap(resMap);
        return tagEndPosition + endKey.length();
    }

    /**
     * 参数
     * @return
     */
    public Map<String, Map<String, String>> getItemMap() {
        Map<String, Map<String, String>> resMap = new HashMap<>();

        String name = this.getPropertyMap().get(ATT_NAME);
        if (name == null || name.trim().length() <= 0) {
            return resMap;
        }
        resMap.put(name, this.getPropertyMap());
        return resMap;
    }

}
