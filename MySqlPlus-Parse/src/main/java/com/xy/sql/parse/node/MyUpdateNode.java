package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MyUpdateNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "update";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MyUpdateNode();
    }



}
