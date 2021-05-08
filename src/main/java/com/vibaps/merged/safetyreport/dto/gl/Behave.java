package com.vibaps.merged.safetyreport.dto.gl;

public class Behave {

	private String	ruleName;
	private Integer	weigt;
	private String rulevalue;
	
	public Behave(String ruleName, Integer weigt) {
		this.ruleName	= ruleName;
		this.weigt		= weigt;
	}
	
	

	public Behave(String ruleName, Integer weigt, String rulevalue) {
		this.ruleName = ruleName;
		this.weigt = weigt;
		this.rulevalue = rulevalue;
	}



	public String getRulevalue() {
		return rulevalue;
	}



	public void setRulevalue(String rulevalue) {
		this.rulevalue = rulevalue;
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
