package com.vibaps.merged.safetyreport.common;

public final class AppConstants {

	public static final String RESPONSE_SUCCESS = "OK";
	//public static final String DATA_MONSTER_BASE_URL="http://localhost:8787/data-monster/";
	public static final String DATA_MONSTER_TRAILER_SEARCH_URL="trailer/search";
	public static final String DATA_MONSTER_TRAILER_SEARCH_COUNT_URL="trailer/count";
	public static final String DATA_MONSTER_DEVICE_SEARCH_URL="device/search";
	public static final String DATA_MONSTER_TRAILERLIST_SEARCH_URL="trailerlist/search";

	public static final String	COLON				= ":";
	public static final String	SLASH				= "/";
	public static final String	PROTOCOL_SECURE		= "https";
	public static final String	PROTOCOL_NON_SECURE	= "http";
	//public static final String  RESULTS_LIMIT="500";
	public static final String PATH_VERSION = "/apiv1";

	public static final String	METHOD_EXECUTE_MULTI_CALL	= "ExecuteMultiCall";
	public static final String	METHOD_GET_REPORT_DATA		= "GetReportData";
	public static final String	METHOD_GET					= "Get";
	public static final String	PARAM_RISK_MANAGEMENT		= "RiskManagement";
	public static final String	PARAM_NONE					= "None";
	public static final String	PARAM_DEVICE				= "Device";
	public static final String	PARAM_SYSTEM_SETTINGS		= "SystemSettings";
	public static final String	FROM_TS_SUFFIX	= "T00:00:01.000Z";
	public static final String	TO_TS_SUFFIX	= "T23:59:59.000Z";
	
	public static final String START_UTC="T00:00:01";
	public static final String END_UTC="T23:59:00";
	
	public static final String[]	DEFAULT_HEADERS		= {"Vehicle Name", "Group", "Distance"};
	public static final String[] NORMAL_REPORT_FORMULAS= { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)","Data!D@","Data!E@","Data!F@",
	        "Data!G@", "Data!H@", "Data!I@", "Data!J@", "Data!K@", "Data!L@", "Data!M@",
	        "IFERROR((E#*E$6)/($C#/100),0)", "IFERROR((F#*F$6)/($C#/100),0)", "IFERROR((G#*G$6)/($C#/100),0)",
	        "IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)",
	        "IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)",
	        "IFERROR((N#*N$6)/($C#/100),0)", "AVERAGE(OFFSET($O#,0,0,1,$Y$5))" };
	
	


	
	public static final String[] TRENDING_REPORT_FORMULAS = { "Data!A@", "Data!B@", "Data!C@", "IF(C#>$C$6,TRUE,FALSE)",
			"DATE(LEFT(Data!E@,4),MID(Data!E@,6,2),MID(Data!E@,9,2))",
			"DATE(LEFT(Data!F@,4),MID(Data!F@,6,2),MID(Data!F@,9,2))", "Data!G@", "Data!H@", "Data!I@", "Data!J@",
			"Data!K@", "Data!L@", "Data!M@", "Data!N@", "Data!O@", "Data!P@", "IFERROR((G#*G$6)/($C#/100),0)",
			"IFERROR((H#*H$6)/($C#/100),0)", "IFERROR((I#*I$6)/($C#/100),0)", "IFERROR((J#*J$6)/($C#/100),0)",
			"IFERROR((K#*K$6)/($C#/100),0)", "IFERROR((L#*L$6)/($C#/100),0)", "IFERROR((M#*M$6)/($C#/100),0)",
			"IFERROR((N#*N$6)/($C#/100),0)", "IFERROR((O#*O$6)/($C#/100),0)", "IFERROR((P#*P$6)/($C#/100),0)",
			"AVERAGE(OFFSET($Q#,0,0,1,$AA$5))" };
	

	
}
