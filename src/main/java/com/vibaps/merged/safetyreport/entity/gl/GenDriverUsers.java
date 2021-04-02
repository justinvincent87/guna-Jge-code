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
@Table(name="gen_driver_users")
public class GenDriverUsers {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "ref_com_database_id")
	private Long refComDatabaseId;
	
	@Column(name = "driver_id")
	private String driverId;
	
	@Column(name = "driver_name")
	private String driverName;	
	
	@Column(name = "emp_number")
	private String empNumber;	
}
