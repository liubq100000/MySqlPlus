package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyIfNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "if";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyIfNode();
    }

    private static final String IF_ATT_COND = "test";


    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        if (MySqlPlusKit.predicate(this.getPropertyMap().get(IF_ATT_COND), paraMap)) {
            List<SqlExePara> res = super.boundSql(context, paraMap);
            return res;
        }
        return new ArrayList<>();
    }
}
