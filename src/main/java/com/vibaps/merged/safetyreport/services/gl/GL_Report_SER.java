package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibaps.merged.safetyreport.dao.gl.GL_Report_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Gl_RulelistEntity;
public class GL_Report_SER {

	@Autowired
	private GL_Report_DAO glReportDao;
	
	public Object insert(ArrayList<String> val, ArrayList<Integer> we, String companyid, String minmiles,String db) {
		ArrayList<Gl_RulelistEntity> v = new ArrayList<>();
		for (int j = 0; j < val.size(); j++) {
			Gl_RulelistEntity ent = new Gl_RulelistEntity();
			ent.setRulevalue(val.get(j));
			ent.setWeight(((Integer)we.get(j)).intValue());
			v.add(ent);
		} 
		return glReportDao.insert(v, companyid, minmiles,db);
	}
	public Object view(String geouserid,String db) {
		// TODO Auto-generated method stub
		return glReportDao.view(geouserid,db);
	}
	public Object viewadd(String geouserid,String db) {
		// TODO Auto-generated method stub
		return glReportDao.viewadd(geouserid,db);
	}
	public Object getgeodropdown(String geouserid) {
		// TODO Auto-generated method stub
		return glReportDao.getgeodropdown(geouserid);
	}

	public Object getLybehave(String geouserid,String db) {
		// TODO Auto-generated method stub
		return glReportDao.getLybehave(geouserid,db);
	}

	public Object viewui(String geouserid,String db) {
		// TODO Auto-generated method stub
		return glReportDao.getallbehaveui(geouserid,db);
	}
	public Object updateresponce(String geouname, String responseJson,String db) {
		// TODO Auto-generated method stub
		
		return glReportDao.updateresponce(geouname,responseJson,db);
	}
	public String selectresponce(String geouname,String db) {
		// TODO Auto-generated method stub
		return glReportDao.selectresponce(geouname,db);
	}
	public Object process(String sees,String sdate,String edate,
		String groupid,String geosees,ArrayList<String> geotabgroups,
		String geouname,String geodatabase,String url,
		String filename,String templect,String enttype,String endpoint) throws MalformedURLException, ParseException, IOException
	{
		return glReportDao.process(sees, sdate, edate, groupid, geosees, geotabgroups, geouname, geodatabase, url, filename,
				templect, enttype,endpoint);
	}
	
	public Object getReportGeo(String sdate,String edate,String geosees,
		ArrayList<String> geotabgroups,String userName,
		String geodatabase,String url,String filename,
		String templect,String enttype)
	{
		return glReportDao.getReportGeo(sdate,edate,geosees,geotabgroups,userName,geodatabase,url,filename,templect,enttype);
	}
	
	public String createExcelReport(String sdate,String edate,String geouname,String geodatabase,String url,
		String filename,String templect) throws EncryptedDocumentException, IOException
	{
		return glReportDao.createExcelReport(sdate,edate,geouname,geodatabase,url,filename,templect);
	}
	

	
	
	

}
