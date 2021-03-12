package com.vibaps.merged.safetyreport.services.truckdown;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.vibaps.merged.safetyreport.dao.truckdown.RepairBookingDAO;
import com.vibaps.merged.safetyreport.dto.truckdown.TruckDownResponce;
import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.entity.truckdown.TrackDownEntity;
import com.vibaps.merged.safetyreport.repo.truckdown.TdUserRepo;



@Service
public class RepairBookingService{
@Autowired
private RepairBookingDAO repairBookingDAO;


@Transactional
public ResponseEntity<String> viewtruckdownresponse(String url) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdownResponce(url);
}

@Transactional
public TruckDownResponce getTruckdowndealer(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdowndealer(entity.getLat(),entity.getLng(),entity.getServicetype());
}

@Transactional
public int getoath() throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdownOauthResponce();
}

@Transactional
public String getTruckPhone(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckPhone(entity.getIds());
}

@Transactional
public String getTruckTiming(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckTiming(entity.getLid());
}

public String getTruckdownservices() throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdownservices();
}


}
