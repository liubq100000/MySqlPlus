package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MyModelNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "model";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyModelNode();
    }


}
