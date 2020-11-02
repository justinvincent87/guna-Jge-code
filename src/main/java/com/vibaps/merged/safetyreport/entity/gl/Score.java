package com.vibaps.merged.safetyreport.entity.gl;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKeyClass;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 * Maintains the score for each Report Row
 */
@Data
@Entity
@Getter
@Setter
@Table(name = "rl_score")
public class Score {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
		private long id;
	@Column(name = "name")
	String name;
    @Column(name = "meanWeightedAvgScores")
	Double meanWeightedAvgScores;
	
    @ElementCollection(targetClass = Double.class)
    @MapKeyClass(String.class)
    Map<String, Double> weightedAvgScores;
	
	public Map<String, Double> getWeightedAvgScores() {
		return weightedAvgScores;
	}
	public void setWeightedAvgScores(Map<String, Double> weightedAvgScores) {
		this.weightedAvgScores = weightedAvgScores;
	}
	public Double getMeanWeightedAvgScores() {
		return meanWeightedAvgScores;
	}
	public void setMeanWeightedAvgScores(Double meanWeightedAvgScores) {
		this.meanWeightedAvgScores = meanWeightedAvgScores;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
