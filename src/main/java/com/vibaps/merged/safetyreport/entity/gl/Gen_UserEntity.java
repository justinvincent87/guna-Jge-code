package com.vibaps.merged.safetyreport.entity.gl;
import java.sql.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="gen_user")
public class Gen_UserEntity {
	public static final String FNN="Gen_User";
	public Gen_UserEntity(){}

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	
	@Column(name = "companyid")
	private String companyid="";
	
	
	public Gen_UserEntity(int id, String companyid) {
		super();
		this.id = id;
		this.companyid = companyid;
	}
	
	
}
