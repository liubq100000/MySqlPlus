package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyIncludeNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "include";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyIncludeNode();
    }

    private String cond;

    public int action(String inSql, int beginIndex) throws Exception {
        String beginKey = "<" + getTag();
        String endKey = "/>";
        int tagBeginPosition = inSql.indexOf(beginKey, beginIndex);
        int tagEndPosition = inSql.indexOf(endKey, tagBeginPosition + beginKey.length());

        String tempCond = inSql.substring(tagBeginPosition + beginKey.length(), tagEndPosition);

        if (tempCond.indexOf("<") > 0 || tempCond.indexOf(">") > 0) {
            throw new Exception("include 标签格式不合法，格式要求：<include refid=\"XXX\"/>");
        }
        Map<String, String> resMap = MySqlPlusKit.parseProperty(tempCond);
        if (!resMap.containsKey("refid")) {
            throw new Exception("include 标签格式不合法，格式要求：<include refid=\"XXX\"/>");
        }
        cond = resMap.get("refid");
        return tagEndPosition + endKey.length();
    }

    /**
     * 构造SQL，绑定参数
     *
     * @param paraMap
     * @return
     * @throws Exception
     */
    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        MySqlPlusNode node = context.getSqlFragmentById(cond);
        resList.addAll(node.boundSql(context,paraMap));
        return resList;
    }
}
