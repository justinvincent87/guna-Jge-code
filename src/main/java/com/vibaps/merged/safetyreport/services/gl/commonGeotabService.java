package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.vibaps.merged.safetyreport.dao.gl.commonGeotabDAO;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
@Service
public class commonGeotabService {
	
     @Autowired
	private commonGeotabDAO dao;

	public Object insertDriver(ReportParams reportParams) throws IOException {
		// TODO Auto-generated method stub

			return dao.insertDriver(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getGeotabSessionId(),reportParams.getUrl());
		
	}

	public Object insertDevice(ReportParams reportParams) throws IOException {
		// TODO Auto-generated method stub
		return dao.insertDevice(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getGeotabSessionId(),reportParams.getUrl());
	}

	public Object getTripRecords(@RequestBody ReportParams reportParams) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		return dao.getTripRecords(reportParams.getGeotabUserName(),reportParams.getGeotabDatabase(),reportParams.getGeotabSessionId(),reportParams.getUrl(),reportParams.getStartDate(),reportParams.getEndDate());
	}



}
