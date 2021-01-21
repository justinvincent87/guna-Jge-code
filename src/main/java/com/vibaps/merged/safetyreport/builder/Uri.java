package com.vibaps.merged.safetyreport.builder;

import static com.vibaps.merged.safetyreport.common.AppConstants.COLON;
import static com.vibaps.merged.safetyreport.common.AppConstants.PROTOCOL_SECURE;
import static com.vibaps.merged.safetyreport.common.AppConstants.SLASH;

public class Uri {

	private StringBuilder builder = new StringBuilder();
	
	public static Uri get() {
		return new Uri();
	}
	
	public Uri secure() {
		builder.append(PROTOCOL_SECURE)
				.append(COLON)
				.append(SLASH)
				.append(SLASH);
		return this;
	}
	
	public Uri add(String path) {
		builder.append(path);
		return this;
	}
	
	public String build() {
		return builder.toString();
	}
}
