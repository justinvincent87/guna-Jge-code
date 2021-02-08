package com.vibaps.merged.safetyreport.services.truckdown;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vibaps.merged.safetyreport.dao.truckdown.RepairBookingDAO;
import com.vibaps.merged.safetyreport.entity.truckdown.TdUser;
import com.vibaps.merged.safetyreport.entity.truckdown.TrackDownEntity;
import com.vibaps.merged.safetyreport.repo.truckdown.TdUserRepo;



@Service
public class RepairBookingService{
@Autowired
private RepairBookingDAO repairBookingDAO;
@Transactional
	public Object view(TrackDownEntity entity) throws MalformedURLException, IOException {
		
		return repairBookingDAO.view(entity.getLat(),entity.getLng(),entity.getServicetype(),entity.getDay());
	}

@Transactional
public String viewtruckdownresponse(String url) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdownResponce(url);
}

@Transactional
public Object getTruckdowndealer(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdowndealer(entity.getLat(),entity.getLng(),entity.getServicetype());
}

@Transactional
public Object getTruckPhone(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckPhone(entity.getIds());
}

@Transactional
public Object getTruckTiming(TrackDownEntity entity) throws MalformedURLException, IOException {
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckTiming(entity.getLid());
}

<<<<<<< HEAD
public Object getTruckdownservices() throws MalformedURLException, IOException {
=======
public Object getTruckdownservices() {
>>>>>>> feature/truckdown
	// TODO Auto-generated method stub
	return repairBookingDAO.getTruckdownservices();
}


}
