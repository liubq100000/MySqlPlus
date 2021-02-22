package com.xy.sql.parse.vo;

public class ExpVO {
	private String cond;
	private String text;
	private int endIndex;

	public ExpVO(String cond, String text, int endIndex) {
		super();
		this.cond = cond;
		this.text = text;
		this.endIndex = endIndex;
	}

	public String getCond() {
		return cond;
	}

	public String getText() {
		if(text == null){
			return "";
		}
		return text;
	}

	public int getEndIndex() {
		return endIndex;
	}

}
