package com.vibaps.merged.safetyreport.api.gl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vibaps.merged.safetyreport.dto.gl.ReportParams;
import com.vibaps.merged.safetyreport.services.gl.RestDriverSafetyReportService;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/glreport" })
public class RestDriverSafetyReport {
<<<<<<< HEAD

	@Autowired
	private RestDriverSafetyReportService glReportService;

	@PostMapping(value = "/insert")
	public Object insert(@RequestBody ReportParams reportParams) {
		return glReportService.insert(reportParams);
=======
	public final GL_Report_SER ser = new GL_Report_SER();
	public final GL_Report_DAO dao = new GL_Report_DAO();
	List<Score> topNRecords;
	List<Score> bottomNRecords;

	static List<String> displayColumns = null;
	static String lytxExceptionSummariesJson = "";
	static Map<Integer, String> lytxBehaviors = null;
	static String lytxBehaviorsJson = "";

	static String getVehicleResponseJson = "";

	private static final int ROW_OFFSET = -1;
	private static final int FORMULA_START_ROW = 7;

	@RequestMapping(value = { "/insert" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object insert(@RequestParam ArrayList<String> val, @RequestParam ArrayList<Integer> we,
			@RequestParam String companyid, @RequestParam String minmiles,@RequestParam String db) {
		return this.ser.insert(val, we, companyid, minmiles,db);
	}

	@RequestMapping(value = { "/getbehave" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object view(@RequestParam String geouserid,@RequestParam String db) {
		return this.ser.view(geouserid,db);
	}

	@RequestMapping(value = { "/getbehaveui" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object viewui(@RequestParam String geouserid,@RequestParam String db) {
		return this.ser.viewui(geouserid,db);
	}

	@RequestMapping(value = { "/getbehaveadd" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object viewadd(@RequestParam String geouserid,@RequestParam String db) {
		return this.ser.viewadd(geouserid,db);
	}

	@RequestMapping(value = { "/getLybehave" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object getLybehave(@RequestParam String geouserid,@RequestParam String db) {
		return this.ser.getLybehave(geouserid,db);
	}

	@RequestMapping(value = { "/getReport" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object getReport(@RequestParam String sees, @RequestParam String sdate, @RequestParam String edate,
			@RequestParam String groupid, @RequestParam String geosees, @RequestParam String geotabgroups,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect, @RequestParam String enttype,@RequestParam String endpoint)
			throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		return dao.process(sees, sdate, edate, groupid, geosees, geotabgroups, geouname, geodatabase, url, filename,
				templect, enttype,endpoint);

	}

	@RequestMapping(value = { "/getReportGeo" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	public Object getReportGeo(@RequestParam String sdate, @RequestParam String edate, @RequestParam String geosees,
			@RequestParam ArrayList<String> geotabgroups, @RequestParam String userName,
			@RequestParam String geodatabase, @RequestParam String url, @RequestParam String filename,
			@RequestParam String templect, @RequestParam String enttype)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String responseJson = "";
		List<Integer> totals = new ArrayList<>();
		Object getgeodropdown = this.ser.getgeodropdown(userName,geodatabase);
		ArrayList<String> getl = (ArrayList<String>) getgeodropdown;
		String value = "";
		Map<String, Map<String, String>> combinedReport = new HashMap<>();
		List<String> displayColumns = null;
		Map<Integer, String> lytxBehaviors = null;
		StringBuffer combinedReportResponseJson = new StringBuffer();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String sDate = sdate;
			String eDate = edate;
			Date ssdate = sdf.parse(sDate);
			Date eedate = sdf.parse(eDate);
			String gvalue = "";
			for (int j = 0; j < getl.size(); j++) {
				if (j != getl.size() - 1) {
					gvalue = gvalue + "{\"id\":\"" + (String) getl.get(j) + "\"},";
				} else {
					gvalue = gvalue + "{\"id\":\"" + (String) getl.get(j) + "\"}";
				}
			}
			String groupvalue = "";
			for (int i = 0; i < geotabgroups.size(); i++) {
				if (i != geotabgroups.size() - 1) {
					groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"},";
				} else {
					groupvalue = groupvalue + "{\"id\":\"" + (String) geotabgroups.get(i) + "\"}";
				}
			}
			String uri = "https://" + url + "/apiv1";
			String urlParameters = "{\"method\":\"ExecuteMultiCall\",\"params\":{\"calls\":[{\"method\":\"GetReportData\",\"params\":{\"argument\":{\"runGroupLevel\":-1,\"isNoDrivingActivityHidden\":true,\"fromUtc\":\""
					+ sdate + "T01:00:00.000Z\",\"toUtc\":\"" + edate + "T03:59:59.000Z\",\"entityType\":\"" + enttype
					+ "\",\"reportArgumentType\":\"RiskManagement\",\"groups\":[" + groupvalue
					+ "],\"reportSubGroup\":\"None\",\"rules\":[" + gvalue
					+ "]}}},{\"method\":\"Get\",\"params\":{\"typeName\":\"SystemSettings\"}}],\"credentials\":{\"database\":\""
					+ geodatabase + "\",\"sessionId\":\"" + geosees + "\",\"userName\":\"" + userName + "\"}}}";

			String serverurl = uri;
			
		//	System.out.println(uri+urlParameters);
			
			
			
			HttpURLConnection con = (HttpURLConnection) (new URL(serverurl)).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", " application/json; charset=utf-8");
			con.setRequestProperty("Content-Language", "en-US");
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setDoInput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			InputStream is = con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			JsonParser parser = new JsonParser();
			JsonObject o = parser.parse(response.toString()).getAsJsonObject();

			String geotabDriverExceptionSummariesJson = "{\"result\":" + o.getAsJsonArray("result").get(0).toString()
					+ "}";

			try {

				// load the header for report data (from the database based on the userName in
				// actual application)
				displayColumns = loadReporColumntHeaders(userName,geodatabase);

				// Load Lytx vehicle map with vehicleId and names

				// Map<Long, String> lytxVehicleList = loadLytxVehicleIDNameMap();
				// lytxBehaviors= loadLytxBehaviors();
				// String[] lytxBehaviorsArray = new String[lytxBehaviors.size()];

				int bCount = 0;

//	        	//create report object with Geotab VEHICLE data:
//	    		Map<String, Map<String, String>> combinedReport = extractGeotabVehicleData(geotabVehicleExceptionSummariesJson);

				// create report object with Geotab DRIVER data:
				if (enttype.equals("Driver")) {
					combinedReport = extractGeotabDriverData(geotabDriverExceptionSummariesJson, userName,geodatabase);
				} else {
					
					combinedReport = extractGeotabVehicleData(geotabDriverExceptionSummariesJson, userName,geodatabase);

					//System.out.println(combinedReport+"-fdf---");
				}

				// create a json response
				totals = new ArrayList<Integer>();
				for (int q = 0; q < displayColumns.size(); q++) {
					totals.add(0);
				}
				combinedReportResponseJson = new StringBuffer();
				combinedReportResponseJson.append("\"information\": [");
				boolean firstRow = true;
				int rulesRecords = displayColumns.size() - 3;
				for (Map.Entry<String, Map<String, String>> combinedReportRows : combinedReport.entrySet()) {
					if (!firstRow) {
						combinedReportResponseJson.append(",");
					} else {
						firstRow = false;
					}
					combinedReportResponseJson.append("{");
					boolean rulesHeadedAdded = false;
					int headerCount = 0;
					int rowCount = 0;
					Map<String, String> rowData = combinedReportRows.getValue();
					for (Map.Entry<String, String> data : rowData.entrySet()) {
						if (headerCount++ > 0 && headerCount < displayColumns.size() + 1) {
							combinedReportResponseJson.append(",");
						}
						if (rowCount++ < 3) {
							rulesHeadedAdded = false;
							combinedReportResponseJson.append("\"" + data.getKey() + "\": \"" + data.getValue() + "\"");
						} else {
							if (!rulesHeadedAdded) {
								combinedReportResponseJson.append("\"Behave\": [");
								rulesHeadedAdded = true;
							}
							combinedReportResponseJson.append("{");
							combinedReportResponseJson.append("\"Rule\": \"" + data.getValue() + "\"}");
							totals.set(rowCount - 1, (totals.get(rowCount - 1) + Integer.parseInt(data.getValue())));
							if (rowCount == displayColumns.size()) {
								combinedReportResponseJson.append("]");
							}
						}

					}
					combinedReportResponseJson.append("}");
				}
				combinedReportResponseJson.append("]}");

				StringBuffer totalsJson = new StringBuffer();
				totalsJson.append("{\"totals\": [");
				int ruleCounter = 0;
				for (int totalVal : totals) {
					totalsJson.append("{ \"Rule\": \"" + totalVal + "\" }");
					ruleCounter++;
					if (ruleCounter != displayColumns.size()) {
						totalsJson.append(",");
					}
				}
				totalsJson.append("],");

				responseJson = totalsJson.toString() + combinedReportResponseJson.toString();
				
//System.out.println(responseJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception exception) {
		}

		try {
			ser.updateresponce(userName, responseJson,geodatabase);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return responseJson;
	}

	@RequestMapping(value = { "/createExcelReport" }, method = { RequestMethod.GET }, produces = { "application/json" })
	@ResponseBody
	private String createExcelReport(@RequestParam String sdate, @RequestParam String edate,
			@RequestParam String geouname, @RequestParam String geodatabase, @RequestParam String url,
			@RequestParam String filename, @RequestParam String templect) throws IOException, FileNotFoundException {
		String responseJson = "";
		try {
			responseJson = ser.selectresponce(geouname,geodatabase);
		} catch (Exception e) {
			// TODO: handle exception
		}

		// top 10 record
		/*
		 * try { topNRecords=dao.calculateTopRecords(geouname,responseJson); }catch
		 * (Exception e) { // TODO: handle exception }
		 * 
		 * //bootom try { bottomNRecords=dao.calculateBottomNRecords(geouname); }catch
		 * (Exception e) { // TODO: handle exception
		 * 
		 * 
		 * }
		 */

		List<String> displayColumns = loadReporColumntHeaders(geouname,geodatabase);

		File source = new File(
				"/home/atiadmin/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
		File dest = new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx");
		try {
			copyFileUsingStream(source, dest);
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		Workbook workbook = WorkbookFactory
				.create(new File("/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/as.xlsx"));
		Sheet sheet = workbook.getSheetAt(0);
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		for (int j3 = 0; j3 < 8; j3++) {
			String name = "";
			String val = "";
			switch (j3) {
			case 0:
				name = "CompanyName";
				val = geodatabase;
				break;
			case 1:
				name = "RunDate";
				val = df.format(calobj.getTime());
				break;
			case 2:
				name = "FromDate";
				val = sdate;
				break;
			case 3:
				name = "ToDate";
				val = edate;
				break;
			}
			sheet.createRow(j3);
			Row row = sheet.getRow(j3);
			Cell cell = row.getCell(0);
			if (cell == null)
				cell = row.createCell(0);
			cell.setCellValue(name);
			cell = row.createCell(1);
			cell.setCellValue(val);
		}
		sheet.createRow(4);
		Row row2 = sheet.getRow(4);
		for (int h = 0; h < displayColumns.size(); h++) {
			Cell cell2 = row2.createCell(h);
			if (h == 0) {
				cell2.setCellValue("Weight");
			} else if (h == 1 || h == 2) {
				cell2.setCellValue("");
			} else {
				int D = 0;
				D = GL_Report_DAO.getwe(geouname, ((String) displayColumns.get(h)).toString().trim(),geodatabase);
				cell2.setCellValue(D);
			}
		}
		sheet.createRow(5);
		Row row3 = sheet.getRow(5);
		for (int j4 = 0; j4 < displayColumns.size(); j4++) {
			Cell cell3 = row3.getCell(j4);
			if (cell3 == null)
				cell3 = row3.createCell(j4);
			cell3.setCellValue(((String) displayColumns.get(j4)).toString());
		}
		JSONObject excelvd = new JSONObject(responseJson);
		JSONArray info = excelvd.getJSONArray("information");
		int s = 0;
		for (s = 0; s < info.length(); s++) {
			sheet.createRow(s + 6);
			Row row4 = sheet.getRow(s + 6);
			Cell cell4 = row4.getCell(0);
			if (cell4 == null)
				cell4 = row4.createCell(0);
			cell4.setCellValue(info.getJSONObject(s).getString("VehicleName"));
			cell4 = row4.createCell(1);
			cell4.setCellValue(info.getJSONObject(s).getString("Group"));
			cell4 = row4.createCell(2);
			cell4.setCellValue(Integer.parseInt(info.getJSONObject(s).getString("Distance")));
			JSONArray behave = info.getJSONObject(s).getJSONArray("Behave");
			for (int h2 = 0; h2 < behave.length(); h2++) {
				cell4 = row4.createCell(h2 + 3);
				cell4.setCellValue(Integer.parseInt(behave.getJSONObject(h2).getString("Rule")));
			}
		}
		Sheet report = workbook.getSheetAt(1);
		Row rows = report.getRow(5);
		Cell cells = rows.getCell(2);
		float min = 0.0F;
		try {
			min = GL_Report_DAO.getminmiles(geouname,geodatabase);
		} catch (Exception exception) {
		}
		cells.setCellValue(min);
		int statrpoint = s + 7;

		String[] formulas = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)","Data!D@","Data!E@", "Data!F@",
				"Data!G@", "Data!H@", "Data!I@", "Data!J@", "Data!K@", "Data!L@", "Data!M@",
				"IFERROR((E#*E$6)/($C#/100),0)", "IFERROR((F#*F$6)/($C#/100),0)", "IFERROR((G#*G$6)/($C#/100),0)",
				"IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)",
				"IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)",
				"IFERROR((N#*N$6)/($C#/100),0)", "AVERAGE(OFFSET($O#,0,0,1,$Y$5))" };
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","AVERAGE(OFFSET($O#,0,0,1,$Y$5))"};
		// String[] formulas =
		// {"Data!A@","Data!B@","Data!C@","IF(C#>$C$6,TRUE,FALSE)","Data!E@","Data!F@","Data!G@","Data!H@","Data!I@","Data!J@","Data!K@","Data!L@","Data!M@","Data!N@","Data!O@","Data!P@","Data!Q@","Data!R@","Data!S@","Data!T@","Data!U@","Data!V@","Data!W@","Data!X@","IFERROR((E#*E$6)/($C8/100),0)","IFERROR((F#*F$6)/($C8/100),0)","IFERROR((G#*G$6)/($C8/100),0)","IFERROR((H#*H$6)/($C8/100),0)","IFERROR((I#*I$6)/($C8/100),0)","IFERROR((J#*J$6)/($C8/100),0)","IFERROR((K#*K$6)/($C8/100),0)","IFERROR((L#*L$6)/($C8/100),0)","IFERROR((M#*M$6)/($C8/100),0)","IFERROR((N#*N$6)/($C8/100),0)","IFERROR((O#*O$6)/($C8/100),0)","IFERROR((P#*P$6)/($C8/100),0)","IFERROR((Q#*Q$6)/($C8/100),0)","IFERROR((R#*R$6)/($C8/100),0)","IFERROR((S#*S$6)/($C8/100),0)","IFERROR((T#*T$6)/($C8/100),0)","IFERROR((U#*U$6)/($C8/100),0)","IFERROR((V#*V$6)/($C8/100),0)","IFERROR((W#*W$6)/($C8/100),0)","IFERROR((X#*X$6)/($C8/100),0)","AVERAGE(OFFSET($Y#,0,0,1,$AS$5))"};
		updateFormulaForReport(report, FORMULA_START_ROW, s, ROW_OFFSET, formulas);
//			

		/*
		 * for (int i = statrpoint; i < Integer.parseInt(templect); i++) { try { Row row
		 * = report.getRow(i); report.removeRow(row); } catch (Exception exception) {} }
		 */

		String newAllDataNamedRange = "Report!$A$7:$Y$" + statrpoint;
		XSSFWorkbook glDSRWorkbook = (XSSFWorkbook) workbook;
		XSSFSheet reportSheet = glDSRWorkbook.getSheet("Report");
		XSSFName allDataNamedRange = glDSRWorkbook.getName("AllData");
		allDataNamedRange.setRefersToFormula(newAllDataNamedRange);

		/*
		 * try { dao.calculateTopBottomNRecords(geouname, responseJson,workbook); }catch
		 * (Exception e) { // TODO: handle exception }
		 */

		  
		 

		try (FileOutputStream outputStream = new FileOutputStream(
				"/usr/local/apache-tomcat-8.5.51/webapps/" + geodatabase + "/report/excel/" + filename + ".xlsx")) {
			XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
			workbook.write(outputStream);
		}
		workbook.close();

		return "{\"url\":\"" + url + geodatabase + "/report/excel/" + filename + ".xlsx\"}";
>>>>>>> feature/truckdown
	}

	@PostMapping(value = "/getbehave")
	public Object view(@RequestBody ReportParams reportParams) {
		return glReportService.view(reportParams);
	}

	@PostMapping(value = "/getbehaveui")
	public Object viewui(@RequestBody ReportParams reportParams) {
		return glReportService.viewui(reportParams);
	}

	@PostMapping(value = "/getbehaveadd")
	public Object viewadd(@RequestBody ReportParams reportParams) {
		return glReportService.viewadd(reportParams);
	}

	@PostMapping(value = "/getLybehave")
	public Object getLybehave(@RequestBody ReportParams reportParams) {
		return glReportService.getLybehave(reportParams);
	}

	@PostMapping(value = "/getReport", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getReport(@RequestBody ReportParams reportParams)
	        throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {

		return glReportService.process(reportParams);

	}

	@PostMapping(value = "/getReportGeo")
	public Object getReportGeo(@RequestBody ReportParams reportParams)
	        throws EncryptedDocumentException, InvalidFormatException, IOException, ParseException {
		return glReportService.getReportGeo(reportParams);
	}

	@PostMapping(value = "/createExcelReport", produces = { "application/string" })
	private String createExcelReport(@RequestBody ReportParams reportParams) throws IOException, FileNotFoundException {

		return glReportService.createExcelReport(reportParams);

	}

}