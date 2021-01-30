package com.vibaps.merged.safetyreport.services.gl;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.dao.gl.Common_Geotab_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Gen_Device;
@Service
public class Common_Geotab_SER {
	
	static Common_Geotab_DAO dao=new Common_Geotab_DAO();

	public Object insertDriver(String geouserid,String databaseName,String geosess,String url) throws IOException {
		// TODO Auto-generated method stub

			return dao.insertDriver(geouserid,databaseName,geosess,url);
		
	}

	public Object insertDevice(String geouserid, String databaseName, String geosess, String url) throws IOException {
		// TODO Auto-generated method stub
		return dao.insertDevice(geouserid,databaseName,geosess,url);
	}

	public Object getTripRecords(String geouserid, String databaseName, String geosess, String url, String sdate,
			String edate) throws MalformedURLException, IOException {
		// TODO Auto-generated method stub
		return dao.getTripRecords(geouserid,databaseName,geosess,url,sdate,edate);
	}



}
