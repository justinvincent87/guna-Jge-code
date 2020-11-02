package com.vibaps.merged.safetyreport.api.gl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.services.gl.Common_Geotab_SER;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/geotab_common_call" })
public class Common_Geotab_API {
    @Autowired
static Common_Geotab_SER ser=new Common_Geotab_SER();


@RequestMapping(value = { "/insertDriver" }, method = { RequestMethod.GET }, produces = { "application/json" })
@ResponseBody
public Object view(@RequestParam String geouserid,@RequestParam String databaseName,@RequestParam String geosess,@RequestParam String url) throws IOException {
	
	return ser.insertDriver(geouserid,databaseName,geosess,url);
}

@RequestMapping(value = { "/insertDevice" }, method = { RequestMethod.GET }, produces = { "application/json" })
@ResponseBody
public Object insertDevice(@RequestParam String geouserid,@RequestParam String databaseName,@RequestParam String geosess,@RequestParam String url) throws IOException {
	
	return ser.insertDevice(geouserid,databaseName,geosess,url);
}

@RequestMapping(value = { "/getTripRecords" }, method = { RequestMethod.GET }, produces = { "application/json" })
@ResponseBody
public Object getTripRecords(@RequestParam String geouserid,@RequestParam String databaseName,@RequestParam String geosess,@RequestParam String url,String sdate,String edate) throws IOException {
	
	return ser.getTripRecords(geouserid,databaseName,geosess,url,sdate,edate);
}

}
