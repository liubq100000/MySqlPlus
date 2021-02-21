package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.fragment.StringFragment;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWhereNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "where";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyWhereNode();
    }

    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        resList.addAll(new StringFragment(" where 1=1 ").boundSql(context, paraMap));
        resList.addAll(super.boundSql(context, paraMap));
        return resList;
    }
}
