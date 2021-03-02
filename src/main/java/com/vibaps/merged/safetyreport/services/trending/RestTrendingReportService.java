package com.vibaps.merged.safetyreport.services.trending;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibaps.merged.safetyreport.dao.trending.RestTrendingReportDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
@Service
public class RestTrendingReportService {
	@Autowired
	private RestTrendingReportDAO restTrendingReportDao;
	
	public Object getReportGeo(ReportParams reportParams) throws ParseException, MalformedURLException, IOException
	{
		return restTrendingReportDao.getReportGeo(reportParams);
	}
	
	public Object getReportGeoLytx(ReportParams reportParams) throws MalformedURLException, IOException
	{
		return restTrendingReportDao.getReportGeoLytx(reportParams.getStartDate(),reportParams.getEndDate(),reportParams.getGeotabSessionId(),reportParams.getGeotabGroups(),reportParams.getGeotabUserName(),
				reportParams.getGeotabDatabase(),reportParams.getUrl(),reportParams.getEntityType(),reportParams.getPeriod());
	}
	
	public String createExcelReport(ReportParams reportParams) throws EncryptedDocumentException, IOException
	{
		return restTrendingReportDao.createExcelReport(reportParams.getStartDate(),reportParams.getEndDate(),reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getUrl(),reportParams.getFilename(),reportParams.getTemplect(),reportParams.getEntityType());
		
	}

}
