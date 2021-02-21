package com.xy.sql.parse.vo;

public class SqlExePara {
	private String sqlFragment;
	private Object value;
	private boolean containNull = false;
	public SqlExePara(String sqlFragment, Object value) {
		super();
		this.sqlFragment = sqlFragment;
		this.value = value;
	}
	public SqlExePara(String sqlFragment, Object value,boolean isContainNull) {
		super();
		this.sqlFragment = sqlFragment;
		this.value = value;
		this.containNull = isContainNull;
	}
	public String getSqlFragment() {
		return sqlFragment;
	}

	public Object getValue() {
		return value;
	}

	public boolean isContainNull() {
		return containNull;
	}

}
