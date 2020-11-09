package com.vibaps.merged.safetyreport.entity.gl;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyClass;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Maintains the score for each Report Row
 */
@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rl_score")
public class Score {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long				id;
	
	@Column(name = "name")
	private String				name;
	
	
	@Column(name = "meanWeightedAvgScores")
	private Double				meanWeightedAvgScores;
	
	@ElementCollection(targetClass = Double.class)
	@MapKeyClass(String.class)
	private Map<String, Double>	weightedAvgScores;
}
