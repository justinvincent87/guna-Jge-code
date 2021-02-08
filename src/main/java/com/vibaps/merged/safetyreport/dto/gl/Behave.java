package com.vibaps.merged.safetyreport.dto.gl;

public class Behave {

	private String	ruleName;
	private Integer	weigt;
	
	public Behave(String ruleName, Integer weigt) {
		this.ruleName	= ruleName;
		this.weigt		= weigt;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getWeigt() {
		return weigt;
	}

	public void setWeigt(Integer weigt) {
		this.weigt = weigt;
	}
}
