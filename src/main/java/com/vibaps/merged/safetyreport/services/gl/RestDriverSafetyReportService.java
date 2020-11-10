package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibaps.merged.safetyreport.dao.gl.GlReportDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.entity.gl.GlRulelistEntity;
@Service
public class RestDriverSafetyReportService {

	@Autowired
	private GlReportDAO glReportDao;
	
	
	public Object insert(ReportParams reportParams) {
		ArrayList<GlRulelistEntity> glRuleListValue = new ArrayList<>();
		for (int j = 0; j < reportParams.getRules().size(); j++) {
			GlRulelistEntity glRulelistEntity = new GlRulelistEntity();
			glRulelistEntity.setRulevalue(reportParams.getRules().get(j));
			glRulelistEntity.setWeight(((Integer)reportParams.getWeight().get(j)).intValue());
			glRuleListValue.add(glRulelistEntity);
		} 
		return glReportDao.insert(glRuleListValue, reportParams.getCompanyId(), reportParams.getMinmiles(),reportParams.getGeotabDatabase());
	}
	
	
	public Object view(ReportParams reportParams) {
		// TODO Auto-generated method stub
		return glReportDao.view(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());
	}
	public Object viewadd(ReportParams reportParams) {
		// TODO Auto-generated method stub
		return glReportDao.viewadd(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());
	}
	public Object getgeodropdown(String geouserid,String geotabDatabase) {
		// TODO Auto-generated method stub
		return glReportDao.getgeodropdown(geouserid,geotabDatabase);
	}

	public Object getLybehave(@RequestBody ReportParams reportParams) {
		// TODO Auto-generated method stub
		return glReportDao.getLybehave(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());
	}

	public Object viewui(ReportParams reportParams) {
		// TODO Auto-generated method stub
		return glReportDao.getallbehaveui(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase());
	}
	public Object updateresponce(String geouname, String responseJson,String db) {
		// TODO Auto-generated method stub
		
		return glReportDao.updateresponce(geouname,responseJson,db);
	}
	public String selectresponce(String geouname,String db) {
		// TODO Auto-generated method stub
		return glReportDao.selectresponce(geouname,db);
	}
	public Object process(@RequestBody ReportParams reportParams) throws MalformedURLException, ParseException, IOException
	{
		return glReportDao.process(reportParams.getLytexSessionid(),reportParams.getStartDate(), reportParams.getEndDate(), reportParams.getGroupId(),reportParams.getGeotabSessionId(),reportParams.getGeotabGroups(),reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(), reportParams.getUrl(),reportParams.getFilename(),reportParams.getTemplect(), reportParams.getEntityType(),reportParams.getEndPoint());
	}
	
	public Object getReportGeo(ReportParams reportParams)
	{
		return glReportDao.getReportGeo(reportParams.getStartDate(),reportParams.getEndDate(),reportParams.getGeotabSessionId(),reportParams.getGeotabGroups(),reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getUrl(),reportParams.getFilename(),reportParams.getTemplect(),reportParams.getEntityType());
	}
	
	public String createExcelReport(@RequestBody ReportParams reportParams) throws EncryptedDocumentException, IOException
	{
		return glReportDao.createExcelReport(reportParams.getStartDate(),reportParams.getEndDate(),reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getUrl(),reportParams.getFilename(),reportParams.getTemplect());
	}
	

	
	
	

}
