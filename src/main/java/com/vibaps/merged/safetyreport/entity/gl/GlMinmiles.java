package com.vibaps.merged.safetyreport.entity.gl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "gl_minmiles")
public class GlMinmiles {
	public static final String FNN = "Gl_Minmiles";
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id = 0;
	@Column(name = "gen_user_id")
	private int gen_user_id = 0;
	@Column(name = "minmiles")
	private float minmiles = 0.0F;
	

	public GlMinmiles(int id, int gen_user_id, float minmiles) {
		this.id = id;
		this.gen_user_id = gen_user_id;
		this.minmiles = minmiles;
	}
	
	
}
