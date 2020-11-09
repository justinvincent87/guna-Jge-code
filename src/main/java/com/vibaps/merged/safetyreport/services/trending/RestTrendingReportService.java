package com.vibaps.merged.safetyreport.services.trending;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibaps.merged.safetyreport.dao.trending.RestTrendingReportDAO;
@Service
public class RestTrendingReportService {
	@Autowired
	private RestTrendingReportDAO restTrendingReportDao;
	
	public Object getReportGeo(String groupid,String sdate,String edate,
			String sees,String geosees,ArrayList<String> geotabgroups,
		String userName,String geodatabase,String url,
		String enttype,String period,String endpoint) throws RemoteException, ParseException
	{
		return restTrendingReportDao.getReportGeo(groupid,sdate,edate,sees,geosees,geotabgroups,userName,geodatabase,url,enttype,period,endpoint);
	}
	
	public Object getReportGeoLytx(String sdate,String edate,String geosees,
		ArrayList<String> geotabgroups,String userName,
		String geodatabase,String url,String enttype, String period)
	{
		return restTrendingReportDao.getReportGeoLytx(sdate,edate,geosees,geotabgroups,userName,
				geodatabase,url,enttype,period);
	}
	
	public String createExcelReport(String sdate,String edate,
			String geouname,String geodatabase,String url,
			String filename,String templect,String entityType) throws EncryptedDocumentException, IOException
	{
		return restTrendingReportDao.createExcelReport(sdate,edate,geouname,geodatabase,url,filename,templect,entityType);
		
	}

}
