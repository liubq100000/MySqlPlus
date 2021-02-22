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
        List<SqlExePara> resList = super.boundSql(context, paraMap);
        if (resList.size() > 0) {
            SqlExePara para;
            for (int i = 0; i < resList.size(); i++) {
                para = resList.get(i);
                String sql = para.getSqlFragment();
                if (sql == null || sql.trim().length() == 0) {
                    continue;
                }
                String sqlUp = sql.toUpperCase();
                int index = sqlUp.indexOf("AND ");
                if (index > 0) {
                    sql = sql.substring(index + 3);
                }
                resList.set(i, new SqlExePara(" WHERE " + sql, para.getValue(), para.isContainNull()));
                break;
            }

        }
        return resList;
    }
}
