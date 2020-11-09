package com.vibaps.merged.safetyreport.entity.gl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rl_reportrow")
public class ReportRow {
	
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long					id;
	
	@Column(name = "name")
	private String					name;
	
	@Column(name = "group")
	private String					group;
	
	@Column(name = "distance")
	private Long					distance;
	
	@Column(name = "selectedRules")
	private Map<String, Integer>	selectedRules	= new LinkedHashMap<>();
}