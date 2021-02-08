package com.vibaps.merged.safetyreport.entity.gl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "gen_device")
public class GenDevice {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "ref_gen_user_id")
	private int refGenUserId;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "device_name")
	private String deviceName;
}
