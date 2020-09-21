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
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name="gen_device")
public class Gen_Device implements Serializable {
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
	
	public static final String FNN="Gen_Device";
	public Gen_Device(){}

	public Gen_Device(int ref_gen_user_id, String device_id, String device_name) {
		super();
		this.ref_gen_user_id = ref_gen_user_id;
		this.device_id = device_id;
		this.device_name = device_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRef_gen_user_id() {
		return ref_gen_user_id;
	}

	public void setRef_gen_user_id(int ref_gen_user_id) {
		this.ref_gen_user_id = ref_gen_user_id;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	};
	
	
}
