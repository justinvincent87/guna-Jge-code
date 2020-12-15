package com.vibaps.merged.safetyreport.services.gl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.vibaps.merged.safetyreport.dao.gl.GL_Report_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Gl_RulelistEntity;
public class GL_Report_SER {

	public final GL_Report_DAO dao=new GL_Report_DAO();
	
	public Object insert(ArrayList<String> val, ArrayList<Integer> we, String companyid, String minmiles,String db) {
		ArrayList<Gl_RulelistEntity> v = new ArrayList<>();
		for (int j = 0; j < val.size(); j++) {
			Gl_RulelistEntity ent = new Gl_RulelistEntity();
			ent.setId(Integer.parseInt(val.get(j)));
			ent.setWeight(((Integer)we.get(j)).intValue());
			v.add(ent);
		} 
		return this.dao.insert(v, companyid, minmiles,db);
	}
	public Object view(String geouserid,String db) {
		// TODO Auto-generated method stub
		return dao.view(geouserid,db);
	}
	public Object viewadd(String geouserid,String db) {
		// TODO Auto-generated method stub
		return dao.viewadd(geouserid,db);
	}
	public Object getgeodropdown(String geouserid,String db) {
		// TODO Auto-generated method stub
		return dao.getgeodropdown(geouserid,db);
	}

	public Object getLybehave(String geouserid,String db) {
		// TODO Auto-generated method stub
		return dao.getLybehave(geouserid,db);
	}

	public Object viewui(String geouserid,String db) {
		// TODO Auto-generated method stub
		return dao.getallbehaveui(geouserid,db);
	}
	public Object updateresponce(String geouname, String responseJson,String db) {
		// TODO Auto-generated method stub
		
		return dao.updateresponce(geouname,responseJson,db);
	}
	public String selectresponce(String geouname,String db) {
		// TODO Auto-generated method stub
		return dao.selectresponce(geouname,db);
	}

}
