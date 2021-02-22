package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MyInsertNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "insert";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyInsertNode();
    }
}
