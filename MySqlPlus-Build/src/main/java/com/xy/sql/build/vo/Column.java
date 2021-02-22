package com.xy.sql.build.vo;

public class Column {
	public static DBType dbType = DBType.oracle;

	private String dataType;
	private String name;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDate() {
		if ("DATE".equalsIgnoreCase(dataType)||"DATETIME".equalsIgnoreCase(dataType)||"TIMESTAMP".equalsIgnoreCase(dataType)) {
			return true;
		}
		return false;
	}

	public boolean isNum() {
		if ("BIGINT".equalsIgnoreCase(dataType) || "INT".equalsIgnoreCase(dataType) || "TINYINT".equalsIgnoreCase(dataType) || "SMALLINT".equalsIgnoreCase(dataType)) {
			return true;
		}
		return false;
	}
	public boolean isDouble() {
		if ("DECIMAL".equalsIgnoreCase(dataType) || "DOUBLE".equalsIgnoreCase(dataType) ) {
			return true;
		}
		return false;
	}
	public String getJavaType() {
		if (this.isDate()) {
			return "Date";
		} else if (this.isNum()) {
			return "Long";
		}
		return "String";
	}


}
