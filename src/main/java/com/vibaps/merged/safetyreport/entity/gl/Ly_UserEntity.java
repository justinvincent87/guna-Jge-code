package com.vibaps.merged.safetyreport.entity.gl;
import java.sql.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

import javax.persistence.Entity;
import javax.persistence.Table;



@Getter
@Entity
@Setter
@Table(name="ly_user")

public class Ly_UserEntity {
	public static final String FNN="Ly_User";
	public Ly_UserEntity(){}

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private int id=0;
	
	@Column(name = "dbname")
	private String dbname="";
	@Column(name = "ly_username")
	private String ly_username="";
	@Column(name = "ly_password")
	private String ly_password="";
}