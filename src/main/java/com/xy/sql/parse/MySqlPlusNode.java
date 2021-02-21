package com.xy.sql.parse;

import com.xy.sql.parse.kit.MySqlPlusKit;
import com.xy.sql.parse.node.MyModelNode;
import com.xy.sql.parse.vo.ContextVO;
import com.xy.sql.parse.vo.ExpVO;
import com.xy.sql.parse.vo.SqlExePara;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL节点
 */
public abstract class MySqlPlusNode {

    private List<MySqlPlusNode> childList;

    //当前节点内容
    private ExpVO expVO;

    private Map<String, String> propertyMap = new HashMap<>();


    /**
     * 取得当前节点得ID
     *
     * @return
     */
    public String getId() {
        String id = propertyMap.get("id");
        if (id == null) {
            return expVO != null ? expVO.getCond() : "";
        }
        return id;
    }

    /**
     * 取得指定SQL片段
     *
     * @param inNextId
     * @return
     */
    public MySqlPlusNode getSqlSegmentById(String inNextId) {
        if (childList == null || inNextId == null) {
            return null;
        }
        for (MySqlPlusNode node : childList) {
            if (inNextId.equalsIgnoreCase(node.getId())) {
                return node;
            }
        }
        return null;
    }



    /**
     * 解析
     *
     * @param inSql
     * @param beginIndex
     * @return
     * @throws Exception
     */
    public int action(String inSql, int beginIndex) throws Exception {
        //分析当前对象
        expVO = MySqlPlusKit.parseNodeText(this.getTag(), inSql, beginIndex);
        //解析子对象
        childList = MySqlPlusKit.parse(expVO.getText().trim());
        //解析表达式
        propertyMap = MySqlPlusKit.parseProperty(expVO.getCond());
        return expVO.getEndIndex();
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
        if (childList != null) {
            for (MySqlPlusNode node : childList) {
                resList.addAll(node.boundSql(context,paraMap));
            }
        }
        return resList;
    }

    /**
     * 标签名称
     * @return
     */
    public abstract String getTag();


    /**
     * 克隆自己
     * @return
     */
    public abstract MySqlPlusNode clone();


    /**
     * 得到所有子对象
     * @return
     */
    public List<MySqlPlusNode> getChildList() {
        return childList;
    }

    /**
     * 得到所有属性
     * @return
     */
    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    /**
     * 设置所有属性
     * @param propertyMap
     */
    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }
}
