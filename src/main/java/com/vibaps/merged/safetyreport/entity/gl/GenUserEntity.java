package com.vibaps.merged.safetyreport.entity.gl;
import java.sql.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="gen_user")
public class GenUserEntity {
	public static final String FNN="Gen_User";
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	
	@Column(name = "companyid")
	private String companyid="";
	
	
	public GenUserEntity(int id, String companyid) {
		super();
		this.id = id;
		this.companyid = companyid;
	}
	
	
}
