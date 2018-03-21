package com.biztweets.model;

public class FollowMetadata {
	private String field;
	private String fieldValue;
	private String operator;
	private String nestedOperator;
	
	public FollowMetadata() {}
	
	public FollowMetadata(String field, String fieldValue, String operator,
			String nestedOperator) {
		super();
		this.field = field;
		this.fieldValue = fieldValue;
		this.operator = operator;
		this.nestedOperator = nestedOperator;
	}

	public String getField() {
		return field;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public String getOperator() {
		return operator;
	}

	public String getNestedOperator() {
		return nestedOperator;
	}

	@Override
	public String toString() {
		return "[field=" + field + ", fieldValue=" + fieldValue
				+ ", operator=" + operator + ", nestedOperator="
				+ nestedOperator + "]";
	}
	
	
}
