package com.vibaps.merged.safetyreport.entity.truckdown;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="td_user")
public class TdUser {
	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="password")
	private String password;
	@Column(name="key")
	private String key;
	@Column(name="secret_key")
	private String secretKey;
	@Column(name="base_url")
	private String baseUrl;
	
	
}
