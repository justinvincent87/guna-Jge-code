package com.vibaps.merged.safetyreport.builder;

public final class GeoTabRequest {

	private String database;
	private String sessionId;
	
	public static GeoTabRequest builder() {
		return new GeoTabRequest();
	}
	
	
}
