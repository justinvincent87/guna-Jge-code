package com.vibaps.merged.safetyreport.common;

public enum EntityType {

	DRIVER ("Driver"),
	DEVICE ("Device");
	
	private String value;
	private EntityType(String value) {
		this.value = value;
	}
	
	public static boolean isDriver(String value) {
		return DRIVER.get().equalsIgnoreCase(value);
	}
	
	public String get() {
		return this.value;
	}
}
