package com.vibaps.merged.safetyreport.services.gl;

import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.dao.gl.RestBaseDAO;

@Service
public class RestBaseService {
@Autowired
private RestBaseDAO restBaseDao;

public Object getlyval(Object vr, Object gv) {
	// TODO Auto-generated method stub
	return restBaseDao.getlyval(vr, gv);
}


	
}
