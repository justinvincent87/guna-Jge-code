package com.vibaps.merged.safetyreport.entity.truckdown;

import java.util.LinkedHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.vibaps.merged.safetyreport.entity.gl.ReportRow;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(name = "trackdown")
public class TrackDownEntity {
@Id
@GeneratedValue(generator = "id")
@GenericGenerator(name = "id", strategy = "increment")
@Column(name = "id", unique = true, nullable = false)
private int id = 0;
@Transient
private Double lat;
@Transient
private Double lng;
@Transient
private String servicetype;
@Transient
private String day;

@Transient
private String ids;

@Transient
private Long lid;
}
