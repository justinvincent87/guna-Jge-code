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
@Table(name = "gen_defects")
public class GenDefects {

	@Id
	@GeneratedValue(generator = "id")
	@GenericGenerator(name = "id", strategy = "increment")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "ref_com_database_id")
	private Long refComDatabaseId;

	@Column(name = "base_defect_id")
	private String baseDefectId;

	@Column(name = "base_defect_name")
	private String baseDefectName;
	
	@Column(name = "defect_id")
	private String defectId;

	@Column(name = "defect_name")
	private String defectName;
}
