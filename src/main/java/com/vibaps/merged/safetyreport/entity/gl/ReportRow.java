package com.vibaps.merged.safetyreport.entity.gl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
<<<<<<< HEAD
	private Long					id;
=======
	 long id;
	@Column(name = "name")
	String name; //Vehicle or driverName
	@Column(name = "group")
	String group;
	@Column(name = "distance")
	long distance;
	
	@Transient
	LinkedHashMap<String, Integer> selectedRules=new LinkedHashMap<String, Integer>();
	 public ReportRow() {

	 }
	
>>>>>>> feature/truckdown
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "group")
	private String	group;
	
	@Column(name = "distance")
	private Long distance;
    @Transient
	private Map<String, Integer> selectedRules	= new LinkedHashMap<>();
}