package com.vibaps.merged.safetyreport.entity.gl;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@Table(name = "tl_tripRecord")
public class Trip {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@Column(name = "vehicleName")
	String vehicleName;
	@Column(name = "driverFirstName")
	String driverFirstName;
	@Column(name = "driverLastName")
	String driverLastName;
	@Column(name = "tripStartTime")
	LocalDateTime tripStartTime;
	@Column(name = "tripEndTime")
	LocalDateTime tripEndTime;

	public Trip(String vehicleName, String driverFirstName, String driverLastName, LocalDateTime tripStartTime, LocalDateTime tripEndTime) {
		this.vehicleName = vehicleName;
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.tripStartTime = tripStartTime;
		this.tripEndTime = tripEndTime;
	}
	
	public Trip() {
		
	}


	

	
	
}
