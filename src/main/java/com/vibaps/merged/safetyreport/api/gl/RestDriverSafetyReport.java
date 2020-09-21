package com.vibaps.merged.safetyreport.api.gl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lytx.dto.ExistingSessionRequest;
import com.lytx.dto.GetBehaviorsResponse;
import com.lytx.dto.GetEventsByLastUpdateDateRequest;
import com.lytx.dto.GetVehiclesRequest;
import com.lytx.dto.GetVehiclesResponse;
import com.lytx.services.ISubmissionServiceV5Proxy;
import com.vibaps.merged.safetyreport.api.gl.RestDriverSafetyReport;
import com.vibaps.merged.safetyreport.dao.gl.GL_Report_DAO;
import com.vibaps.merged.safetyreport.entity.gl.Score;
import com.vibaps.merged.safetyreport.services.gl.GL_Report_SER;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "*" }, allowedHeaders = { "*" })
@RestController
@RequestMapping({ "/glreport" })
public class RestDriverSafetyReport {
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
			@RequestParam String groupid, @RequestParam String geosees, @RequestParam ArrayList<String> geotabgroups,
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
		Object getgeodropdown = this.ser.getgeodropdown(userName);
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
				"/usr/local/apache-tomcat-8.5.51/webapps/GL_Driver_Safety_Report_Template_" + templect + ".xlsx");
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

		String[] formulas = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)", "Data!E@", "Data!F@",
				"Data!G@", "Data!H@", "Data!I@", "Data!J@", "Data!K@", "Data!L@", "Data!M@", "Data!N@",
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
	}

	public static String toStringValue(Object object) {
		String value = "";
		if (object != null)
			if (object instanceof String) {
				value = (String) object;
			} else {
				try {
					value = "" + object.toString().trim();
				} catch (Exception exception) {
				}
			}
		return value;
	}

	public static List<String> loadReporColumntHeaders(String userName,String db) {
		List<String> reportColumnHeader = new ArrayList<>();
		reportColumnHeader.add("VehicleName");
		reportColumnHeader.add("Group");
		reportColumnHeader.add("Distance");
		GL_Report_DAO da = new GL_Report_DAO();
		ArrayList<String> gval = new ArrayList();
		gval = da.getallbehave(userName,db);
		for (int j = 0; j < gval.size(); j++) {
			// System.out.println(j + "-----" + gval.get(j));
			reportColumnHeader.add(gval.get(j));
		}
		//System.out.println(reportColumnHeader.size() + "-----");
		return reportColumnHeader;
	}

	public static Map<Integer, String> loadLytxBehaviors(String lytxBehaviorsJson) {
		Map<Integer, String> lBehaviors = new HashMap<>();
		JSONObject lytxBehaviorsJO = new JSONObject(lytxBehaviorsJson);
		JSONArray lytxBehaviorsArray = lytxBehaviorsJO.getJSONArray("behaviors");
		for (int i = 0; i < lytxBehaviorsArray.length(); i++) {
			JSONObject behaviorJO = lytxBehaviorsArray.getJSONObject(i);
			int lytxBehaviorId = behaviorJO.getInt("behaviorId");
			String lytxDescription = "L-" + behaviorJO.getString("description");
			lBehaviors.put(Integer.valueOf(lytxBehaviorId), lytxDescription);
		}
		return lBehaviors;
	}

	public static Map<Long, String> loadLytxVehicleIDNameMap(String getVehicleResponseJson) {
		Map<Long, String> lytxVehicleList = new HashMap<>();
		JSONObject lytxVehiclesJO = new JSONObject(getVehicleResponseJson);
		JSONArray lytxVehiclesArray = lytxVehiclesJO.getJSONArray("vehicles");
		for (int i = 0; i < lytxVehiclesArray.length(); i++) {
			String vehicleName = lytxVehiclesArray.getJSONObject(i).getString("name");
			Long vehicleId = Long.valueOf(lytxVehiclesArray.getJSONObject(i).getLong("vehicleId"));
			lytxVehicleList.put(vehicleId, vehicleName);
		}
		return lytxVehicleList;
	}

	public static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0)
				os.write(buffer, 0, length);
		} finally {
			is.close();
			os.close();
		}
	}

	public static boolean vehiclename(String vehiclename, ArrayList vehiclearray) {
		boolean vehiclestatus = false;
		for (int i = 0; i < vehiclearray.size(); i++) {
			if (vehiclearray.get(i).toString() == vehiclename) {
				vehiclestatus = true;
				break;
			}
		}

		return vehiclestatus;
	}

	private static void updateFormulaForReport(Sheet sheet, int startRow, int numberOfRows, int rowOffset,
			String[] formulaList) {

		for (int i = startRow; i < startRow + numberOfRows; i++) {

			Row curRow = sheet.createRow(i);
			for (int col = 0; col < formulaList.length; col++) {
				Cell newCell = curRow.createCell(col);
				String formula = formulaList[col];
				formula = formula.replace("@", Integer.toString(i));
				formula = formula.replace("#", Integer.toString(i + 1));
				newCell.setCellFormula(formula);
			}
		}
	}

	private static void updatedCombinedReportWithLytxExceptions(Map<String, Map<String, String>> combinedReport,
			Map<String, Map<String, Integer>> lytxVehicleEventsRecord) {
		// for every vehicle in lytxVehicleEventsRecord
		for (Map.Entry<String, Map<String, Integer>> lytxVehiclesEventsMapEntry : lytxVehicleEventsRecord.entrySet()) {
			// Get the report row corresponding to that vehicle.
			String lytxVehicleName = lytxVehiclesEventsMapEntry.getKey();
			Map<String, String> reportHeader = combinedReport.get(lytxVehicleName);
			// if the lytxVehicle is not in the Geotab's vehicle list, then skip.
			if (reportHeader == null) {
				continue;
			}
			Map<String, Integer> lytxVehExceptions = lytxVehiclesEventsMapEntry.getValue();
			for (int m = 3; m < displayColumns.size(); m++) {
				if (lytxVehExceptions.get(displayColumns.get(m)) != null) {
					reportHeader.put(displayColumns.get(m), (lytxVehExceptions.get(displayColumns.get(m))).toString());
				}
			}
		}
	}

	/**
	 * @param lytxVehicleList
	 * @param lytxVehicleEventsRecord
	 */
	private static Map<String, Map<String, Integer>> extractExceptionDataFromLytxResponse(
			Map<Long, String> lytxVehicleList) {

		Map<String, Map<String, Integer>> lytxVehicleEventsRecord = new HashMap<String, Map<String, Integer>>();
		// Process lytxExceptionSummariesJson
		JSONObject lytxEventsJO = new JSONObject(lytxExceptionSummariesJson);
		JSONArray lytxEventsArray = lytxEventsJO.getJSONArray("events");
		for (int i = 0; i < lytxEventsArray.length(); i++) {
			Long eventsVehicleId = lytxEventsArray.getJSONObject(i).getLong("vehicleId");
			String vehicleName = lytxVehicleList.get(eventsVehicleId);
			Map<String, Integer> lytxExceptionEvents = lytxVehicleEventsRecord.get(vehicleName);
			if (lytxExceptionEvents == null) {
				lytxExceptionEvents = new HashMap<String, Integer>();
				lytxVehicleEventsRecord.put(vehicleName, lytxExceptionEvents);
			}
			JSONArray lytxBehavioursArray = lytxEventsArray.getJSONObject(i).getJSONArray("behaviors");
			for (int j = 0; j < lytxBehavioursArray.length(); j++) {
				int behavior = lytxBehavioursArray.getJSONObject(j).getInt("behavior");
				String exceptionName = lytxBehaviors.get(behavior);
				Integer behaviorCount = lytxExceptionEvents.get(exceptionName);
				if (behaviorCount == null) {
					behaviorCount = 0;
				}
				lytxExceptionEvents.put(exceptionName, ++behaviorCount);
			}
		}
		return lytxVehicleEventsRecord;
	}

	/**
	 * @param combinedReport
	 */
	private static Map<String, Map<String, String>> extractGeotabVehicleData(String geotabVehicleExceptionSummariesJson,
			String userName,String db) {
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();

		try {

			List<String> displayColumns = loadReporColumntHeaders(userName,db);

			// create report object:
			// GEOTAB Events processing
			JSONObject geotabEventsJO = new JSONObject(geotabVehicleExceptionSummariesJson);
			JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
			for (int i = 0; i < geotabEventsJOArray.length(); i++) {
				JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
				JSONObject itemJO = resultsChild.getJSONObject("item");

				// vehicleName
				String geotabVehicleName = itemJO.getString("name");
				Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();
				newReportRow.put(displayColumns.get(0), geotabVehicleName);
				// group
				JSONArray geotabVehicleGroups = itemJO.getJSONArray("groups");
				String group = null;
				for (int j = 0; j < geotabVehicleGroups.length(); j++) {
					if (group == null) {
						try {
							group = geotabVehicleGroups.getJSONObject(j).getString("name");
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else {

						try {
							String newGroup = geotabVehicleGroups.getJSONObject(j).getString("name");
							if ("Prohibit Idling".equalsIgnoreCase(newGroup)) {
								group = newGroup + ", " + group;
							} else {
								group = group + ", " + newGroup;
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
				newReportRow.put(displayColumns.get(1), group);
				// Distance
				Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");

				long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

				newReportRow.put(displayColumns.get(2), Long.toString(tDistance));

				// Geotab exceptions from exceptionSummaries
				Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
				JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
				for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
					
					try {
					int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
					JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
							.getJSONObject("exceptionRule");
					String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
				
				//	System.out.println("-----guna"+geotabExceptionName);

					geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				for (int m = 3; m < displayColumns.size(); m++) {
					if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
						System.out.println(displayColumns.get(m)+"----notnull");
						
						newReportRow.put(displayColumns.get(m),
								(geotabExceptionEvents.get(displayColumns.get(m))).toString());
						System.out.println(displayColumns.get(m)+"----not111111null");

					} else {

						if (newReportRow.get(displayColumns.get(m)) == null) {
							
							newReportRow.put(displayColumns.get(m), "0");
						}
					}
				}
				
				System.out.println(geotabVehicleName+"----"+newReportRow);
				
				combinedReport.put(geotabVehicleName, newReportRow);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return combinedReport;
	}

	/**
	 * @param combinedReport
	 */
	private static Map<String, Map<String, String>> extractGeotabDriverData(String geotabDriverExceptionSummariesJson,
			String userName,String db) {

		List<String> displayColumns = loadReporColumntHeaders(userName,db);

		// create report object:
		Map<String, Map<String, String>> combinedReport = new LinkedHashMap<String, Map<String, String>>();
		// GEOTAB Events processing
		JSONObject geotabEventsJO = new JSONObject(geotabDriverExceptionSummariesJson);
		JSONArray geotabEventsJOArray = geotabEventsJO.getJSONArray("result");
		for (int i = 0; i < geotabEventsJOArray.length(); i++) {
			JSONObject resultsChild = geotabEventsJOArray.getJSONObject(i);
			JSONObject itemJO = resultsChild.getJSONObject("item");
			// driverName
			String geotabVehicleName = itemJO.getString("firstName") + " " + itemJO.getString("lastName");
			Map<String, String> newReportRow = new LinkedHashMap<String, String>();// getNewReportRow();

			newReportRow.put(displayColumns.get(0), geotabVehicleName);
			// group
			JSONArray geotabDriverGroups = itemJO.getJSONArray("driverGroups");
			String group = null;
			for (int j = 0; j < geotabDriverGroups.length(); j++) {
				if (group == null) {
					group = geotabDriverGroups.getJSONObject(j).getString("name");
				} else {
					group = group + ", " + geotabDriverGroups.getJSONObject(j).getString("name");
					;
				}
			}
			newReportRow.put(displayColumns.get(1), group);
			// Distance
			Object geotabVehicleTotalDistance = resultsChild.get("totalDistance");

			long tDistance = ((Double) geotabVehicleTotalDistance).longValue();

			newReportRow.put(displayColumns.get(2), Long.toString(tDistance));

			// Geotab exceptions from exceptionSummaries
			Map<String, Integer> geotabExceptionEvents = new HashMap<String, Integer>();
			JSONArray geotabExceptionSummariesJA = resultsChild.getJSONArray("exceptionSummaries");
			for (int k = 0; k < geotabExceptionSummariesJA.length(); k++) {
				try
				{
				int eventCount = geotabExceptionSummariesJA.getJSONObject(k).getInt("eventCount");
				JSONObject geotabExceptionRuleJO = geotabExceptionSummariesJA.getJSONObject(k)
						.getJSONObject("exceptionRule");
				String geotabExceptionName = "G-" + geotabExceptionRuleJO.getString("name");
				geotabExceptionEvents.put(geotabExceptionName, geotabExceptionEvents.get(geotabExceptionName)==null?eventCount:geotabExceptionEvents.get(geotabExceptionName)+eventCount);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			for (int m = 3; m < displayColumns.size(); m++) {
				if (geotabExceptionEvents.get(displayColumns.get(m)) != null) {
					newReportRow.put(displayColumns.get(m),
							(geotabExceptionEvents.get(displayColumns.get(m))).toString());
				} else {
					if (newReportRow.get(displayColumns.get(m)) == null) {
						newReportRow.put(displayColumns.get(m), "0");
					}
				}
			}
			combinedReport.put(geotabVehicleName, newReportRow);
		}
		return combinedReport;
	}

	public static Map<String, String> getNewReportRow(String geoname,String db) {
		// Initialize displayColumns values the first time
		if (displayColumns == null) {
			displayColumns = loadReporColumntHeaders(geoname,db);
		}
		// create report object:
		Map<String, String> reportRow = new HashMap<String, String>();
		for (String column : displayColumns) {
			reportRow.put(column, null);
		}
		return reportRow;
	}

	public static Map<Integer, String> loadLytxBehaviors() {
		Map<Integer, String> lBehaviors = new HashMap<Integer, String>();
		JSONObject lytxBehaviorsJO = new JSONObject(lytxBehaviorsJson);
		JSONArray lytxBehaviorsArray = lytxBehaviorsJO.getJSONArray("behaviors");
		for (int i = 0; i < lytxBehaviorsArray.length(); i++) {
			JSONObject behaviorJO = lytxBehaviorsArray.getJSONObject(i);
			int lytxBehaviorId = behaviorJO.getInt("behaviorId");
			String lytxDescription = "L-" + behaviorJO.getString("description");
			lBehaviors.put(lytxBehaviorId, lytxDescription);
		}
//        printLytxBehaviors(lBehaviors);
		return lBehaviors;
	}

	public static void printLytxBehaviors(Map<Integer, String> lBehaviors) {

		for (Map.Entry<Integer, String> lBehavior : lBehaviors.entrySet()) {
			System.out.println(lBehavior.getKey() + " | " + lBehavior.getValue());
		}

	}

	public static Map<Long, String> loadLytxVehicleIDNameMap() {
		Map<Long, String> lytxVehicleList = new HashMap<Long, String>();
		JSONObject lytxVehiclesJO = new JSONObject(getVehicleResponseJson);
		JSONArray lytxVehiclesArray = lytxVehiclesJO.getJSONArray("vehicles");
		for (int i = 0; i < lytxVehiclesArray.length(); i++) {
			String vehicleName = lytxVehiclesArray.getJSONObject(i).getString("name");
			Long vehicleId = lytxVehiclesArray.getJSONObject(i).getLong("vehicleId");
			lytxVehicleList.put(vehicleId, vehicleName);
		}
		return lytxVehicleList;
	}

}