package com.xy.sql.parse.node;

import com.xy.sql.parse.MySqlPlusNode;

public class MySelectNode extends MySqlPlusNode {

	@Override
	public String getTag() {
		return "select";
	}

	@Override
	public MySqlPlusNode clone() {
		return new MySelectNode();
	}
}
