package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.fragment.StringFragment;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyForeachNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "foreach";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyForeachNode();
    }

    //collection="formIdList" index="index" item="formIdListValue" open="(" separator="," close=")"
    private static final String FOR_ATT_COLLECTION = "collection";

    private static final String FOR_ATT_ITEM = "item";

    private static final String FOR_ATT_SEPARATOR = "separator";

    private static final String FOR_ATT_OPEN = "open";

    private static final String FOR_ATT_CLOSE = "close";

    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> resList = new ArrayList<>();
        String collection = this.getPropertyMap().get(FOR_ATT_COLLECTION);
        if (collection == null || collection.toString().trim().length() < 0) {
            return resList;
        }
        String key = this.getPropertyMap().get(FOR_ATT_ITEM);
        if (key == null || key.trim().length() < 0) {
            return resList;
        }
        Object collectionValue = paraMap.get(collection);
        if (collectionValue == null || collectionValue.toString().trim().length() < 0) {
            return resList;
        }

        String[] values = collectionValue.toString().trim().split(",");

        String open = this.getPropertyMap().get(FOR_ATT_OPEN);
        if (open != null && open.trim().length() > 0) {
            resList.addAll(new StringFragment(open).boundSql(context,paraMap));
        }
        String separator = this.getPropertyMap().get(FOR_ATT_SEPARATOR);
        int i = -1;
        for (String value : values) {
            i++;
            paraMap.put(key, value);
            List<SqlExePara> listTemp = null;
            if (i != 0 && key != null && key.trim().length() > 0) {
                resList.addAll(new StringFragment(separator).boundSql(context,paraMap));
            }
            for (MySqlPlusNode temp : this.getChildList()) {
                listTemp = temp.boundSql(context,paraMap);
                if (listTemp != null && listTemp.size() > 0) {
                    resList.addAll(listTemp);
                }
            }
        }
        String close = this.getPropertyMap().get(FOR_ATT_CLOSE);
        if (close != null && close.trim().length() > 0) {
            resList.addAll(new StringFragment(close).boundSql(context,paraMap));
        }
        return resList;
    }
}
