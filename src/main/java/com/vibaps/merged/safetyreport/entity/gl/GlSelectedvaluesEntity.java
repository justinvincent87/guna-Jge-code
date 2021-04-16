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
@Table(name = "gl_selectedvalues")
public class GlSelectedvaluesEntity {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id")
	private Long		id;
	
	@Column(name = "gen_user_id")
	private Long	gen_user_id;
	
	@Column(name = "gen_rulelist_id")
	private Long	gen_rulelist_id;

	@Column(name = "weight")
	private int		weight;

	@Column(name = "status")
	private int status;

	public GlSelectedvaluesEntity(Long gen_rulelist_id, int weight, int status) {
		this.gen_rulelist_id = gen_rulelist_id;
		this.weight = weight;
		this.status = status;
	}
	

}
