package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
import com.vibaps.merged.safetyreport.service.gl.GlReportService;


@Service
public class RestDriverSafetyReportService {

	@Autowired
	private GlReportService glReportService;

	public Object insert(ReportParams reportParams) {
		ArrayList<GlRulelistEntity> glRuleListValue = new ArrayList<>();
		for (int j = 0; j < reportParams.getRules().size(); j++) {
			GlRulelistEntity glRulelistEntity = new GlRulelistEntity();
			glRulelistEntity.setId(((Integer) reportParams.getRuleId().get(j)).intValue());
			glRulelistEntity.setWeight(((Integer) reportParams.getWeight().get(j)).intValue());
			glRuleListValue.add(glRulelistEntity);
		}
		return glReportService.insert(glRuleListValue, reportParams.getCompanyId(), reportParams.getMinmiles(),
		        reportParams.getGeotabDatabase());
	}

	public Object view(ReportParams reportParams) {
		return glReportService.view(reportParams.getGeotabUserName(), reportParams.getGeotabDatabase());
	}

	public Object viewadd(ReportParams reportParams) {
		return glReportService.viewadd(reportParams.getGeotabUserName(), reportParams.getGeotabDatabase());
	}

	public Object getgeodropdown(String geouserid, String geotabDatabase) {
		return glReportService.getgeodropdown(geouserid, geotabDatabase);
	}

	public Object getLybehave(@RequestBody ReportParams reportParams) {
		return glReportService.getLybehave(reportParams.getGeotabUserName(), reportParams.getGeotabDatabase());
	}

	public Object viewui(ReportParams reportParams) {
		return glReportService.getallbehaveui(reportParams.getGeotabUserName(), reportParams.getGeotabDatabase());
	}

	public Object updateresponce(String geouname, String responseJson, String db) {
		return glReportService.updateresponce(geouname, responseJson, db);
	}

	public String selectresponce(String geouname, String db) {
		return glReportService.selectresponce(geouname, db);
	}

	public Object process(ReportParams reportParams)
	        throws MalformedURLException, ParseException, IOException {
		return glReportService.process(reportParams);
	}

	public Object getReportGeo(ReportParams reportParams) throws MalformedURLException, IOException, ParseException {
		return glReportService.getReportGeo(reportParams.getStartDate(), reportParams.getEndDate(),
		        reportParams.getGeotabSessionId(), reportParams.getGeotabGroups(), reportParams.getGeotabUserName(),
		        reportParams.getGeotabDatabase(), reportParams.getUrl(), reportParams.getFilename(),
		        reportParams.getTemplect(), reportParams.getEntityType());
	}

	public String createExcelReport(@RequestBody ReportParams reportParams)
	        throws EncryptedDocumentException, IOException {
		return glReportService.createExcelReport(reportParams.getStartDate(), reportParams.getEndDate(),
		        reportParams.getGeotabUserName(), reportParams.getGeotabDatabase(), reportParams.getUrl(),
		        reportParams.getFilename(), reportParams.getTemplect());
	}

}
