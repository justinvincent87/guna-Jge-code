package com.vibaps.merged.safetyreport.entity.gl;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gl_minmiles")
public class GlMinmiles {
	
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "gen_user_id")
	private Integer genUserId;
	
	@Column(name = "minmiles")
	private Float minmiles;
}
