package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MySqlNode extends MySqlPlusNode {

    @Override
    public String getTag() {
        return "sql";
    }

    @Override
    public MySqlPlusNode clone() {
        return new MySqlNode();
    }


}
