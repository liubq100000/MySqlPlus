package com.xy.sql.parse.fragment;

import com.xy.sql.parse.kit.MySqlStandard;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StringFragment implements IFragment {

    private String oriSql;

    public StringFragment(String inSql) {
        this.oriSql = inSql;

    }

    @Override
    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) {
        return Arrays.asList(new SqlExePara(" " + MySqlStandard.sqlPreProcess(this.oriSql) + " ", null));
    }

}
