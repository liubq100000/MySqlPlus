package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MyDeleteNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "delete";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyDeleteNode();
    }

}
