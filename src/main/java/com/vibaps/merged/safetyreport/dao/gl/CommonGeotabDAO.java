package com.vibaps.merged.safetyreport.dao.gl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerResponce;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.entity.gl.GenDevice;
import com.vibaps.merged.safetyreport.entity.gl.GenDriver;
import com.vibaps.merged.safetyreport.entity.gl.GenTrailer;
import com.vibaps.merged.safetyreport.repo.gl.ComDatabaseRepository;
import com.vibaps.merged.safetyreport.repo.gl.CommonGeotabRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenDeviceRepository;
import com.vibaps.merged.safetyreport.repo.gl.GenTrailerRepository;
import com.vibaps.merged.safetyreport.service.gl.GlReportService;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;
import com.vibaps.merged.safetyreport.services.trailer.TrailerService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CommonGeotabDAO  {

	@Autowired
	private CommonGeotabRepository commonGeotabRepository;
	@Autowired
	private ComDatabaseRepository comDatabaseRepository;
	@Autowired
	private GenDeviceRepository genDeviceRepository;
	@Autowired
	private GenTrailerRepository genTrailereRepository;
	@Autowired
	private CommonGeotabService commonGeotabService;
	
	
	
	
	private ComDatabase comDatabase;
	
	
	

@Transactional	
public ComDatabase insertDevice(TrailerParams reportParams){

	comDatabase=comDatabaseRepository.findBydatabaseName(reportParams.getGeotabDatabase());
	
	if(comDatabase==null)
	{  
		comDatabase=new ComDatabase();
		comDatabase.setDatabaseName(reportParams.getGeotabDatabase());
		comDatabase=comDatabaseRepository.save(comDatabase);
	}

	return insertGeoTabDevice(comDatabase.getId(),reportParams);
	}

@Transactional	
public ComDatabase insertTrailer(TrailerParams reportParams){

	comDatabase=comDatabaseRepository.findBydatabaseName(reportParams.getGeotabDatabase());
	
	if(comDatabase==null)
	{  
		comDatabase=new ComDatabase();
		comDatabase.setDatabaseName(reportParams.getGeotabDatabase());
		comDatabase=comDatabaseRepository.save(comDatabase);
	}

	return insertGeoTabTrailer(comDatabase.getId(),reportParams);
	}


private ComDatabase insertGeoTabDevice(Long id, TrailerParams reportParams)
{

	TrailerResponce trailerResponce=commonGeotabService.getDevice(reportParams);
	List<GenDevice> collection=new ArrayList<GenDevice>();
	for(int i=0;i<trailerResponce.getResult().size();i++)
	{
		GenDevice param=new GenDevice();
		param.setRefComDatabaseId(id);
		param.setDeviceId(trailerResponce.getResult().get(i).getId());
		param.setDeviceName(trailerResponce.getResult().get(i).getName());
		collection.add(param);
	}
	genDeviceRepository.saveAll(collection);
	
	comDatabase.setResult("Saved");
	return comDatabase;
}

public GenDevice insertMissedGeoTabDevice(Long id, TrailerParams reportParams,String deviceId)
{

	TrailerResponce trailerResponce=commonGeotabService.getMissedDevice(reportParams,deviceId);
	List<GenDevice> collection=new ArrayList<GenDevice>();
	GenDevice param=new GenDevice();
	for(int i=0;i<1;i++)
	{
		
		param.setRefComDatabaseId(id);
		param.setDeviceId(trailerResponce.getResult().get(i).getId());
		param.setDeviceName(trailerResponce.getResult().get(i).getName());
		param=genDeviceRepository.save(param);
		//collection.add(param);
	}
	
	
	
	return param;
}

public GenTrailer insertGeoTabMissedTrailer(Long id, TrailerParams reportParams,String trailerId) 
{

	TrailerResponce trailerResponce=commonGeotabService.getMissedTrailer(reportParams,trailerId);
	
		GenTrailer param=new GenTrailer();
		param.setRefComDatabaseId(id);
		param.setTrailerId(trailerResponce.getResult().get(0).getId());
		param.setTrailerName(trailerResponce.getResult().get(0).getName());
		
		log.info("Trailer Insert Info {}",id+" - "+trailerResponce.getResult().get(0).getId()+" - "+trailerResponce.getResult().get(0).getName());
		
		param=genTrailereRepository.save(param);
	
	
	return param;
}

private ComDatabase insertGeoTabTrailer(Long id, TrailerParams reportParams) 
{

	TrailerResponce trailerResponce=commonGeotabService.getTrailer(reportParams);
	List<GenTrailer> collection=new ArrayList<GenTrailer>();
	for(int i=0;i<trailerResponce.getResult().size();i++)
	{
		GenTrailer param=new GenTrailer();
		param.setRefComDatabaseId(id);
		param.setTrailerId(trailerResponce.getResult().get(i).getId());
		param.setTrailerName(trailerResponce.getResult().get(i).getName());
		collection.add(param);
	}
	genTrailereRepository.saveAll(collection);
	
	comDatabase.setResult("Saved");
	return comDatabase;
}


}
