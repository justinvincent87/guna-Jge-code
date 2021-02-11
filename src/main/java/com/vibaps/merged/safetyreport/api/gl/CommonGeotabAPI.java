package com.vibaps.merged.safetyreport.api.gl;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.dto.trailer.TrailerParams;
import com.vibaps.merged.safetyreport.entity.gl.ComDatabase;
import com.vibaps.merged.safetyreport.services.gl.CommonGeotabService;
import com.vibaps.merged.safetyreport.util.FileUtil;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/geotab_common_call" })
public class CommonGeotabAPI {

@Autowired
private CommonGeotabService commonGeotabService;




@PostMapping(value = "/insertDevice")
public ComDatabase insertDevice(@RequestBody TrailerParams reportParams) throws SQLException{
	
	return commonGeotabService.insertDevice(reportParams);
}
@PostMapping(value = "/insertTrailer")
public ComDatabase insertTrailer(@RequestBody TrailerParams reportParams) throws SQLException{
	
	return commonGeotabService.insertTrailer(reportParams);
}

@PostMapping(value = "/uploadFile")
public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String filePath) throws SQLException, IOException{
	
	return commonGeotabService.uploadFile(file,filePath+FileUtil.fileExtention(file.getOriginalFilename())+"/");
}

@PostMapping(value = "/apiCallUsingURL")
public ResponseEntity<String> uploadFile(@RequestParam String url) {
	
	return commonGeotabService.apiCallUsingURL(url);
}



}
