package com.vibaps.merged.safetyreport.entity.gl;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name="gl_rulelist")
@SecondaryTable(name = "gl_selectedvalues")
public class GlRulelistEntity {
	
	
	
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	@Column(name="rulename")
	private String rulename="";
	@Column(name="rulevalue")
	private String rulevalue="";
	@Column(name="rulecompany")
	private String rulecompany="";
	@Column(name="weight",length = 12)
	private int weight;
	
	 @Column(name = "status", table = "gl_selectedvalues")
	  private int status=0;

	public GlRulelistEntity(int id, String rulename, String rulevalue, String rulecompany,int status,int weight) {
		super();
		this.id = id;
		this.rulename = rulename;
		this.rulevalue = rulevalue;
		this.rulecompany = rulecompany;
		this.status=status;
		this.weight=weight;
	}
	
	public GlRulelistEntity(String rulename,int weight) {
	
		this.rulename = rulename;
		
		this.weight=weight;
	}

	public GlRulelistEntity(String rulevalue) {
		super();
		this.rulevalue = rulevalue;
	}
	
	


	public GlRulelistEntity(int weight) {
		super();
		this.weight = weight;
	}

}
