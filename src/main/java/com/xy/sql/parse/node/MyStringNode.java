package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;
import com.xy.sql.parse.fragment.IFragment;
import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.kit.MySqlStandard;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyStringNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyStringNode();
    }

    private String sqlTxt;

    private List<IFragment> fragmentList;

    public int action(String inSql, int beginIndex) throws Exception {
        int tagPosition = inSql.indexOf(MySqlStandard.XML_START_CHAR, beginIndex);
        if (tagPosition < 0) {
            sqlTxt = inSql.substring(beginIndex);
            fragmentList = MySqlPlusKit.processFragment(sqlTxt);
            return inSql.length();
        }
        if (tagPosition == 0) {
            if (beginIndex + MySqlStandard.MAX_TAG_LEN < inSql.length()) {
                throw new Exception("解析异常，该标签不识别:" + inSql.substring(beginIndex, beginIndex + MySqlStandard.MAX_TAG_LEN));
            } else {
                throw new Exception("解析异常，该标签不识别:" + inSql.substring(beginIndex));
            }
        }
        sqlTxt = inSql.substring(beginIndex, tagPosition);
        fragmentList = MySqlPlusKit.processFragment(sqlTxt);
        return tagPosition;
    }

    public List<SqlExePara> boundSql(ContextVO context, Map<String, Object> paraMap) throws Exception {
        List<SqlExePara> list = new ArrayList<>();
        List<SqlExePara> listTemp;
        if (fragmentList != null) {
            for (IFragment fo : fragmentList) {
                listTemp = fo.boundSql(context,paraMap);
                if (listTemp != null && listTemp.size() > 0) {
                    list.addAll(listTemp);
                }
            }
        }
        return list;
    }

}
