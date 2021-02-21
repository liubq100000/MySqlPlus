package com.xy.sql.parse.fragment;

import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldFragment implements IFragment {
    private static final String FIELD_ATT_JDBCTYPE = "jdbctype";

    private static final String FIELD_DOUBLE = "DOUBLE";

    private static final String FIELD_INT = "INT";

    private static final String FIELD_LONG = "LONG";

    private static final String FIELD_BIGINT = "BIGINT";

    private static final String FIELD_DATE = "DATE";

    private static final String FIELD_DATETIME = "DATETIME";

    private static final String FIELD_FUNCTION = "function";

    private String oriSql;

    private String fieldName;

    private boolean direct;

    private Map<String, String> propertyMap = new HashMap<>();

    public FieldFragment(String inSql, boolean direct) {
        this.oriSql = inSql;
        this.direct = direct;
        String tempSql = oriSql.trim();
        String fieldStr = tempSql.substring(2, tempSql.length() - 1);
        fieldStr = fieldStr.trim();
        fieldStr = fieldStr.replace("\"", "");
        fieldStr = fieldStr.replace("\'", "");
        String[] names = fieldStr.split(",");
        fieldName = names[0].trim();
        if (names.length > 1) {
            String att;
            String[] attKeyValue;
            for (int i = 1; i < names.length; i++) {
                att = names[i].trim();
                attKeyValue = att.split("=");
                if (attKeyValue.length > 1) {
                    propertyMap.put(attKeyValue[0].trim().toLowerCase(), attKeyValue[1].trim());
                } else {
                    propertyMap.put(attKeyValue[0].trim().toLowerCase(), "");
                }

            }
        }
    }

    /**
     * 取得值
     *
     * @param value
     * @return
     */
    private Object getValue(Object value) {
        String type = propertyMap.get(FIELD_ATT_JDBCTYPE);
        return MySqlPlusKit.convertValue(type, value);
    }

    @Override
    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) {


        Object value = paraMap.get(fieldName);
        Object newValue = getValue(value);
        if (newValue == null) {
            return Arrays.asList(new SqlExePara("?", null, true));
        }
        if (direct) {
            return Arrays.asList(new SqlExePara(newValue.toString(), null));
        }
        return Arrays.asList(new SqlExePara(" ? ", getValue(value), true));

    }

}
