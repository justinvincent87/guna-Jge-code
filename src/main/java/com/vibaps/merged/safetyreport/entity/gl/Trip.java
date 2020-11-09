package com.vibaps.merged.safetyreport.entity.gl;

import java.time.LocalDateTime;

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
@Table(name = "tl_tripRecord")
public class Trip {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "vehicleName")
	private String vehicleName;

	@Column(name = "driverFirstName")
	private String driverFirstName;

	@Column(name = "driverLastName")
	private String driverLastName;

	@Column(name = "tripStartTime")
	private LocalDateTime tripStartTime;

	@Column(name = "tripEndTime")
	private LocalDateTime tripEndTime;

	public Trip(String vehicleName, String driverFirstName, String driverLastName, LocalDateTime tripStartTime,
	        LocalDateTime tripEndTime) {
		this.vehicleName		= vehicleName;
		this.driverFirstName	= driverFirstName;
		this.driverLastName		= driverLastName;
		this.tripStartTime		= tripStartTime;
		this.tripEndTime		= tripEndTime;
	}
}
