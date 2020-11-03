package com.vibaps.merged.safetyreport.dto.gl;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReportParams {

	private ArrayList<String> val;
	private ArrayList<Integer> we;
	private String companyid;
	private String minmiles;
	private String db;
	private String geouserid;

}
