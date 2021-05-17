package com.vibaps.merged.safetyreport.entity.gl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "gl_rulelist")
public class GlRulelistEntityI {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "rulename")
	private String rulename;
	
	@Column(name = "rulevalue")
	private String rulevalue;
	
	@Column(name = "rulecompany")
	private String rulecompany;
	
	@Column(name = "db")
	private String db;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "weight")
	private Integer weight;
}
