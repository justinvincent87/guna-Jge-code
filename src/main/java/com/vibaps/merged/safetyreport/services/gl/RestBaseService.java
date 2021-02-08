package com.vibaps.merged.safetyreport.services.gl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibaps.merged.safetyreport.dao.gl.RestBaseDAO;

@Service
public class RestBaseService {
	
	@Autowired
	private RestBaseDAO restBaseDao;

	public Object getlyval(Object vr, Object gv) {
		return restBaseDao.getlyval(vr, gv);
	}
}
