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

}
