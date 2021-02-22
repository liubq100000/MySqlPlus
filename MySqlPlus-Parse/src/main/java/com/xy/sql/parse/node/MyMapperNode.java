package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMapperNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "mapper";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyMapperNode();
    }

    public String getNamespace(){
        return this.getPropertyMap().get("namespace");
    }


    //模型
    private MyModelNode model;

    //模型属性
    private Map<String, Map<String, String>> itemMap;

    /**
     * 取得模型
     *
     * @return
     */
    public MyModelNode getModel() {
        if (model == null) {

            if (this.getChildList() == null) {
                return null;
            }
            for (MySqlPlusNode node : this.getChildList()) {
                if (node instanceof MyModelNode) {
                    model = (MyModelNode) node;
                }
            }
        }
        return model;
    }

    /**
     * 取得模型信息
     *
     * @return
     */
    public Map<String, Map<String, String>> getItemMap() {
        if (itemMap == null) {
            itemMap = new HashMap<>();
            List<MySqlPlusNode> itemList = getModel().getChildList();
            if (itemList != null) {
                for (MySqlPlusNode item : itemList) {
                    itemMap.putAll(((MyModelItemNode) item).getItemMap());
                }
            }
        }
        return itemMap;
    }
}
