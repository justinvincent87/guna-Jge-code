package com.vibaps.merged.safetyreport.entity.gl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jws.Oneway;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Getter
@Setter
@Table(name = "rl_reportrow")
public class ReportRow{
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	 long id;
	@Column(name = "name")
	String name; //Vehicle or driverName
	@Column(name = "group")
	String group;
	@Column(name = "distance")
	long distance;
	@Column(name = "selectedRules")
	LinkedHashMap<String, Integer> selectedRules=new LinkedHashMap<String, Integer>();
	 public ReportRow() {

	 }
	
	
	/*
	 * @OneToOne(mappedBy = "rl_reportrow",cascade = CascadeType.ALL) Score score;
	 */
	

	
}