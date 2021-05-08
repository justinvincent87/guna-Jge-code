package com.vibaps.merged.safetyreport.entity.gl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gl_rulelist")
@SecondaryTable(name = "gl_selectedvalues")
public class GlRulelistEntity {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long		id;
	
	@Column(name = "rulename")
	private String	rulename;
	
	@Column(name = "rulevalue")
	private String	rulevalue;
	
	@Column(name = "rulecompany")
	private String	rulecompany;
	
	@Column(name = "weight")
	private int		weight;

	@Column(name = "status", table = "gl_selectedvalues")
	private int status;
	

	
	@Transient
	private Float minmiles;
	


	public GlRulelistEntity(String rulename, int weight) {
		this.rulename = rulename;
		this.weight = weight;
	}

	
	  public GlRulelistEntity(String rulevalue) 
	  { 
		  this.rulevalue = rulevalue; 
		  
	  }
	 

	public GlRulelistEntity(int weight) {
		this.weight = weight;
	}


	public GlRulelistEntity(String rulename, String rulevalue) {
		this.rulename = rulename;
		this.rulevalue = rulevalue;
	}








}
