package com.timss.finance.util;

public enum StatusEnum {
	DRAFT("D"),APPROVING("AI"),APPROVED("AE"),VOIDED("V");
	private String name;
	private StatusEnum(String name){
		this.name=name;
	}
	public String toString(){
		return name;
	}
}
