package com.vibaps.merged.safetyreport.entity.gl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.cache.annotation.CacheConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Entity
@Setter
@AllArgsConstructor

@Table(name="gen_driver")
public class Gen_Driver {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	@Column(name = "ref_gen_user_id")
	private int ref_gen_user_id;
	@Column(name = "driver_id")
	private String driver_id="";
	@Column(name = "driver_name")
	private String driver_name="";
	public Gen_Driver() {
		// TODO Auto-generated constructor stub
	}
	
	
}
