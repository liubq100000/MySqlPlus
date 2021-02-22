package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySetNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "set";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MySetNode();
    }

    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        List<SqlExePara> dataList = super.boundSql(context, paraMap);
        resList.add(new SqlExePara(" set ", null));
        if (dataList.size() > 0) {

            SqlExePara para;
            for (int i = dataList.size() - 1; i >= 0; i--) {
                para = dataList.get(i);
                String sql = para.getSqlFragment();
                if (sql == null || sql.trim().length() == 0) {
                    continue;
                }
                sql = sql.trim();
                if (sql.endsWith(",")) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                dataList.set(i, new SqlExePara(" " + sql + " ", para.getValue(), para.isContainNull()));
                break;
            }
            resList.addAll(dataList);
        }

        return resList;
    }
}
