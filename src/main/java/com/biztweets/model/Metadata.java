package com.biztweets.model;

public class Metadata {
	private String key;
	private String value;
	
	public Metadata() {}
	
	
	public Metadata(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}


	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}


	@Override
	public String toString() {
		return "[key=" + key + ", value=" + value + "]";
	}
	
	
}
