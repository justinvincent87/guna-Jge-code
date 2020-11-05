package com.vibaps.merged.safetyreport.entity.gl;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name="gen_device")
public class genDevice implements Serializable {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	@Column(name = "ref_gen_user_id")
	private int ref_gen_user_id;
	@Column(name = "device_id")
	private String device_id="";
	@Column(name = "device_name")
	private String device_name="";
	
	
	
}
