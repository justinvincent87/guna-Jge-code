package com.vibaps.merged.safetyreport.entity.gl;

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
@Table(name = "gl_selectedvalues")
public class UserReportFilterEntity {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "gen_user_id")
	private int userId;
	

	@Column(name = "gen_rulelist_id")
	private int ruleId;
	
	@Column(name = "status")
	private int status;
	
	@Column(name="weight")
	private int weight;
}
