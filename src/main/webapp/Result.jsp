<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleISubmissionServiceV5Proxyid" scope="session" class="com.lytx.services.ISubmissionServiceV5Proxy" />
<%
	if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleISubmissionServiceV5Proxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
	String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleISubmissionServiceV5Proxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp%>
<%
	}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
%>
        <%=tempResultreturnp3%>
        <%
        	}
                break;
                case 5:
                        gotMethod = true;
                        String endpoint_0id=  request.getParameter("endpoint8");
                            java.lang.String endpoint_0idTemp = null;
                        if(!endpoint_0id.equals("")){
                         endpoint_0idTemp  = endpoint_0id;
                        }
                        sampleISubmissionServiceV5Proxyid.setEndpoint(endpoint_0idTemp);
                break;
                case 10:
                        gotMethod = true;
                        com.lytx.services.ISubmissionServiceV5 getISubmissionServiceV510mtemp = sampleISubmissionServiceV5Proxyid.getISubmissionServiceV5();
                if(getISubmissionServiceV510mtemp == null){
        %>
<%=getISubmissionServiceV510mtemp %>
<%
}else{
        if(getISubmissionServiceV510mtemp!= null){
        String tempreturnp11 = getISubmissionServiceV510mtemp.toString();
        %>
        <%=tempreturnp11%>
        <%
        }}
break;
case 13:
        gotMethod = true;
        java.lang.String ping13mtemp = sampleISubmissionServiceV5Proxyid.ping();
if(ping13mtemp == null){
%>
<%=ping13mtemp %>
<%
}else{
        String tempResultreturnp14 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(ping13mtemp));
        %>
        <%= tempResultreturnp14 %>
        <%
        	}
        break;
        case 16:
                gotMethod = true;
                String userName_1id=  request.getParameter("userName27");
                    java.lang.String userName_1idTemp = null;
                if(!userName_1id.equals("")){
                 userName_1idTemp  = userName_1id;
                }
                String password_2id=  request.getParameter("password29");
                    java.lang.String password_2idTemp = null;
                if(!password_2id.equals("")){
                 password_2idTemp  = password_2id;
                }
                com.lytx.dto.LoginResponse login16mtemp = sampleISubmissionServiceV5Proxyid.login(userName_1idTemp,password_2idTemp);
        if(login16mtemp == null){
        %>
<%=login16mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">companyId:</TD>
<TD>
<%
	if(login16mtemp != null){
java.lang.Long typecompanyId19 = login16mtemp.getCompanyId();
        String tempResultcompanyId19 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typecompanyId19));
%>
        <%=tempResultcompanyId19%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(login16mtemp != null){
java.lang.Boolean typeerror21 = login16mtemp.getError();
        String tempResulterror21 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror21));
%>
        <%=tempResulterror21%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(login16mtemp != null){
java.lang.String typemessage23 = login16mtemp.getMessage();
        String tempResultmessage23 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage23));
%>
        <%=tempResultmessage23%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">sessionId:</TD>
<TD>
<%
	if(login16mtemp != null){
java.lang.String typesessionId25 = login16mtemp.getSessionId();
        String tempResultsessionId25 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typesessionId25));
%>
        <%=tempResultsessionId25%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 31:
        gotMethod = true;
        String userName_3id=  request.getParameter("userName42");
            java.lang.String userName_3idTemp = null;
        if(!userName_3id.equals("")){
         userName_3idTemp  = userName_3id;
        }
        String password_4id=  request.getParameter("password44");
            java.lang.String password_4idTemp = null;
        if(!password_4id.equals("")){
         password_4idTemp  = password_4id;
        }
        String companyId_5id=  request.getParameter("companyId46");
            java.lang.Long companyId_5idTemp = null;
        if(!companyId_5id.equals("")){
         companyId_5idTemp  = java.lang.Long.valueOf(companyId_5id);
        }
        com.lytx.dto.LoginResponse loginCompany31mtemp = sampleISubmissionServiceV5Proxyid.loginCompany(userName_3idTemp,password_4idTemp,companyId_5idTemp);
if(loginCompany31mtemp == null){
%>
<%=loginCompany31mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">companyId:</TD>
<TD>
<%
	if(loginCompany31mtemp != null){
java.lang.Long typecompanyId34 = loginCompany31mtemp.getCompanyId();
        String tempResultcompanyId34 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typecompanyId34));
%>
        <%=tempResultcompanyId34%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(loginCompany31mtemp != null){
java.lang.Boolean typeerror36 = loginCompany31mtemp.getError();
        String tempResulterror36 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror36));
%>
        <%=tempResulterror36%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(loginCompany31mtemp != null){
java.lang.String typemessage38 = loginCompany31mtemp.getMessage();
        String tempResultmessage38 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage38));
%>
        <%=tempResultmessage38%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">sessionId:</TD>
<TD>
<%
	if(loginCompany31mtemp != null){
java.lang.String typesessionId40 = loginCompany31mtemp.getSessionId();
        String tempResultsessionId40 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typesessionId40));
%>
        <%=tempResultsessionId40%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 48:
        gotMethod = true;
        String sessionId_6id=  request.getParameter("sessionId59");
            java.lang.String sessionId_6idTemp = null;
        if(!sessionId_6id.equals("")){
         sessionId_6idTemp  = sessionId_6id;
        }
        String dispatchEntries_7id=  request.getParameter("dispatchEntries61");
            java.lang.String dispatchEntries_7idTemp = null;
        if(!dispatchEntries_7id.equals("")){
         dispatchEntries_7idTemp  = dispatchEntries_7id;
        }
        com.lytx.dto.SubmitDriverDispatchLogsResponse submitDriverDispatchLogs48mtemp = sampleISubmissionServiceV5Proxyid.submitDriverDispatchLogs(sessionId_6idTemp,dispatchEntries_7idTemp);
if(submitDriverDispatchLogs48mtemp == null){
%>
<%=submitDriverDispatchLogs48mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">validationResponse:</TD>
<TD>
<%
	if(submitDriverDispatchLogs48mtemp != null){
java.lang.String typevalidationResponse51 = submitDriverDispatchLogs48mtemp.getValidationResponse();
        String tempResultvalidationResponse51 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typevalidationResponse51));
%>
        <%=tempResultvalidationResponse51%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(submitDriverDispatchLogs48mtemp != null){
java.lang.Boolean typeerror53 = submitDriverDispatchLogs48mtemp.getError();
        String tempResulterror53 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror53));
%>
        <%=tempResulterror53%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(submitDriverDispatchLogs48mtemp != null){
java.lang.String typemessage55 = submitDriverDispatchLogs48mtemp.getMessage();
        String tempResultmessage55 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage55));
%>
        <%=tempResultmessage55%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">submissionId:</TD>
<TD>
<%
	if(submitDriverDispatchLogs48mtemp != null){
java.lang.Long typesubmissionId57 = submitDriverDispatchLogs48mtemp.getSubmissionId();
        String tempResultsubmissionId57 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typesubmissionId57));
%>
        <%=tempResultsubmissionId57%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 63:
        gotMethod = true;
        String sessionId_8id=  request.getParameter("sessionId72");
            java.lang.String sessionId_8idTemp = null;
        if(!sessionId_8id.equals("")){
         sessionId_8idTemp  = sessionId_8id;
        }
        String submissionId_9id=  request.getParameter("submissionId74");
            java.lang.Long submissionId_9idTemp = null;
        if(!submissionId_9id.equals("")){
         submissionId_9idTemp  = java.lang.Long.valueOf(submissionId_9id);
        }
        com.lytx.dto.GetSubmissionStatusResponse getSubmissionStatus63mtemp = sampleISubmissionServiceV5Proxyid.getSubmissionStatus(sessionId_8idTemp,submissionId_9idTemp);
if(getSubmissionStatus63mtemp == null){
%>
<%=getSubmissionStatus63mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">submissionStatus:</TD>
<TD>
<%
	if(getSubmissionStatus63mtemp != null){
java.lang.Long typesubmissionStatus66 = getSubmissionStatus63mtemp.getSubmissionStatus();
        String tempResultsubmissionStatus66 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typesubmissionStatus66));
%>
        <%=tempResultsubmissionStatus66%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getSubmissionStatus63mtemp != null){
java.lang.Boolean typeerror68 = getSubmissionStatus63mtemp.getError();
        String tempResulterror68 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror68));
%>
        <%=tempResulterror68%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getSubmissionStatus63mtemp != null){
java.lang.String typemessage70 = getSubmissionStatus63mtemp.getMessage();
        String tempResultmessage70 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage70));
%>
        <%=tempResultmessage70%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 76:
        gotMethod = true;
        String sessionId_10id=  request.getParameter("sessionId85");
            java.lang.String sessionId_10idTemp = null;
        if(!sessionId_10id.equals("")){
         sessionId_10idTemp  = sessionId_10id;
        }
        String submissionId_11id=  request.getParameter("submissionId87");
            java.lang.Long submissionId_11idTemp = null;
        if(!submissionId_11id.equals("")){
         submissionId_11idTemp  = java.lang.Long.valueOf(submissionId_11id);
        }
        com.lytx.dto.GetSubmissionResultsResponse getSubmissionResults76mtemp = sampleISubmissionServiceV5Proxyid.getSubmissionResults(sessionId_10idTemp,submissionId_11idTemp);
if(getSubmissionResults76mtemp == null){
%>
<%=getSubmissionResults76mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getSubmissionResults76mtemp != null){
java.lang.Boolean typeerror79 = getSubmissionResults76mtemp.getError();
        String tempResulterror79 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror79));
%>
        <%=tempResulterror79%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getSubmissionResults76mtemp != null){
java.lang.String typemessage81 = getSubmissionResults76mtemp.getMessage();
        String tempResultmessage81 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage81));
%>
        <%=tempResultmessage81%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">submissionResults:</TD>
<TD>
<%
	if(getSubmissionResults76mtemp != null){
java.lang.String typesubmissionResults83 = getSubmissionResults76mtemp.getSubmissionResults();
        String tempResultsubmissionResults83 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typesubmissionResults83));
%>
        <%=tempResultsubmissionResults83%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 89:
        gotMethod = true;
        String includeSubgroups_13id=  request.getParameter("includeSubgroups100");
            java.lang.Boolean includeSubgroups_13idTemp = null;
        if(!includeSubgroups_13id.equals("")){
         includeSubgroups_13idTemp  = java.lang.Boolean.valueOf(includeSubgroups_13id);
        }
        String groupId_14id=  request.getParameter("groupId102");
            java.lang.Long groupId_14idTemp = null;
        if(!groupId_14id.equals("")){
         groupId_14idTemp  = java.lang.Long.valueOf(groupId_14id);
        }
        String sessionId_15id=  request.getParameter("sessionId104");
            java.lang.String sessionId_15idTemp = null;
        if(!sessionId_15id.equals("")){
         sessionId_15idTemp  = sessionId_15id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1GetVehiclesRequest_12id" scope="session" class="com.lytx.dto.GetVehiclesRequest" />
        <%
        	com1DriveCam1Classes1GetVehiclesRequest_12id.setIncludeSubgroups(includeSubgroups_13idTemp);
                com1DriveCam1Classes1GetVehiclesRequest_12id.setGroupId(groupId_14idTemp);
                com1DriveCam1Classes1GetVehiclesRequest_12id.setSessionId(sessionId_15idTemp);
                com.lytx.dto.GetVehiclesResponse getVehicles89mtemp = sampleISubmissionServiceV5Proxyid.getVehicles(com1DriveCam1Classes1GetVehiclesRequest_12id);
        if(getVehicles89mtemp == null){
        %>
<%=getVehicles89mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getVehicles89mtemp != null){
java.lang.Boolean typeerror92 = getVehicles89mtemp.getError();
        String tempResulterror92 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror92));
%>
        <%=tempResulterror92%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">vehicles:</TD>
<TD>
<%
	if(getVehicles89mtemp != null){
com.lytx.dto.VehicleInfo[] typevehicles94 = getVehicles89mtemp.getVehicles();
        String tempvehicles94 = null;
        if(typevehicles94 != null){
        java.util.List listvehicles94= java.util.Arrays.asList(typevehicles94);
        tempvehicles94 = listvehicles94.toString();
        }
%>
        <%=tempvehicles94%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getVehicles89mtemp != null){
java.lang.String typemessage96 = getVehicles89mtemp.getMessage();
        String tempResultmessage96 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage96));
%>
        <%=tempResultmessage96%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 106:
        gotMethod = true;
        String sessionId_17id=  request.getParameter("sessionId117");
            java.lang.String sessionId_17idTemp = null;
        if(!sessionId_17id.equals("")){
         sessionId_17idTemp  = sessionId_17id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1ExistingSessionRequest_16id" scope="session" class="com.lytx.dto.ExistingSessionRequest" />
        <%
        	com1DriveCam1Classes1ExistingSessionRequest_16id.setSessionId(sessionId_17idTemp);
                com.lytx.dto.GetVehicleTypesResponse getVehicleTypes106mtemp = sampleISubmissionServiceV5Proxyid.getVehicleTypes(com1DriveCam1Classes1ExistingSessionRequest_16id);
        if(getVehicleTypes106mtemp == null){
        %>
<%=getVehicleTypes106mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">vehicleTypes:</TD>
<TD>
<%
	if(getVehicleTypes106mtemp != null){
com.lytx.dto.VehicleType[] typevehicleTypes109 = getVehicleTypes106mtemp.getVehicleTypes();
        String tempvehicleTypes109 = null;
        if(typevehicleTypes109 != null){
        java.util.List listvehicleTypes109= java.util.Arrays.asList(typevehicleTypes109);
        tempvehicleTypes109 = listvehicleTypes109.toString();
        }
%>
        <%=tempvehicleTypes109%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getVehicleTypes106mtemp != null){
java.lang.Boolean typeerror111 = getVehicleTypes106mtemp.getError();
        String tempResulterror111 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror111));
%>
        <%=tempResulterror111%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getVehicleTypes106mtemp != null){
java.lang.String typemessage113 = getVehicleTypes106mtemp.getMessage();
        String tempResultmessage113 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage113));
%>
        <%=tempResultmessage113%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 119:
        gotMethod = true;
        String sessionId_19id=  request.getParameter("sessionId130");
            java.lang.String sessionId_19idTemp = null;
        if(!sessionId_19id.equals("")){
         sessionId_19idTemp  = sessionId_19id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1ExistingSessionRequest_18id" scope="session" class="com.lytx.dto.ExistingSessionRequest" />
        <%
        	com1DriveCam1Classes1ExistingSessionRequest_18id.setSessionId(sessionId_19idTemp);
                com.lytx.dto.GetVehicleStatusesResponse getVehicleStatuses119mtemp = sampleISubmissionServiceV5Proxyid.getVehicleStatuses(com1DriveCam1Classes1ExistingSessionRequest_18id);
        if(getVehicleStatuses119mtemp == null){
        %>
<%=getVehicleStatuses119mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getVehicleStatuses119mtemp != null){
java.lang.Boolean typeerror122 = getVehicleStatuses119mtemp.getError();
        String tempResulterror122 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror122));
%>
        <%=tempResulterror122%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">vehicleStatuses:</TD>
<TD>
<%
	if(getVehicleStatuses119mtemp != null){
com.lytx.dto.VehicleStatus[] typevehicleStatuses124 = getVehicleStatuses119mtemp.getVehicleStatuses();
        String tempvehicleStatuses124 = null;
        if(typevehicleStatuses124 != null){
        java.util.List listvehicleStatuses124= java.util.Arrays.asList(typevehicleStatuses124);
        tempvehicleStatuses124 = listvehicleStatuses124.toString();
        }
%>
        <%=tempvehicleStatuses124%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getVehicleStatuses119mtemp != null){
java.lang.String typemessage126 = getVehicleStatuses119mtemp.getMessage();
        String tempResultmessage126 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage126));
%>
        <%=tempResultmessage126%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 132:
        gotMethod = true;
        String sessionId_21id=  request.getParameter("sessionId143");
            java.lang.String sessionId_21idTemp = null;
        if(!sessionId_21id.equals("")){
         sessionId_21idTemp  = sessionId_21id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1ExistingSessionRequest_20id" scope="session" class="com.lytx.dto.ExistingSessionRequest" />
        <%
        	com1DriveCam1Classes1ExistingSessionRequest_20id.setSessionId(sessionId_21idTemp);
                com.lytx.dto.GetEventTypesResponse getEventTypes132mtemp = sampleISubmissionServiceV5Proxyid.getEventTypes(com1DriveCam1Classes1ExistingSessionRequest_20id);
        if(getEventTypes132mtemp == null){
        %>
<%=getEventTypes132mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getEventTypes132mtemp != null){
java.lang.Boolean typeerror135 = getEventTypes132mtemp.getError();
        String tempResulterror135 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror135));
%>
        <%=tempResulterror135%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getEventTypes132mtemp != null){
java.lang.String typemessage137 = getEventTypes132mtemp.getMessage();
        String tempResultmessage137 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage137));
%>
        <%=tempResultmessage137%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">eventTypes:</TD>
<TD>
<%
	if(getEventTypes132mtemp != null){
com.lytx.dto.EventType[] typeeventTypes139 = getEventTypes132mtemp.getEventTypes();
        String tempeventTypes139 = null;
        if(typeeventTypes139 != null){
        java.util.List listeventTypes139= java.util.Arrays.asList(typeeventTypes139);
        tempeventTypes139 = listeventTypes139.toString();
        }
%>
        <%=tempeventTypes139%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 145:
        gotMethod = true;
        String sessionId_23id=  request.getParameter("sessionId156");
            java.lang.String sessionId_23idTemp = null;
        if(!sessionId_23id.equals("")){
         sessionId_23idTemp  = sessionId_23id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1ExistingSessionRequest_22id" scope="session" class="com.lytx.dto.ExistingSessionRequest" />
        <%
        	com1DriveCam1Classes1ExistingSessionRequest_22id.setSessionId(sessionId_23idTemp);
                com.lytx.dto.GetEventStatusesResponse getEventStatuses145mtemp = sampleISubmissionServiceV5Proxyid.getEventStatuses(com1DriveCam1Classes1ExistingSessionRequest_22id);
        if(getEventStatuses145mtemp == null){
        %>
<%=getEventStatuses145mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">eventStatuses:</TD>
<TD>
<%
	if(getEventStatuses145mtemp != null){
com.lytx.dto.EventStatus[] typeeventStatuses148 = getEventStatuses145mtemp.getEventStatuses();
        String tempeventStatuses148 = null;
        if(typeeventStatuses148 != null){
        java.util.List listeventStatuses148= java.util.Arrays.asList(typeeventStatuses148);
        tempeventStatuses148 = listeventStatuses148.toString();
        }
%>
        <%=tempeventStatuses148%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getEventStatuses145mtemp != null){
java.lang.Boolean typeerror150 = getEventStatuses145mtemp.getError();
        String tempResulterror150 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror150));
%>
        <%=tempResulterror150%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getEventStatuses145mtemp != null){
java.lang.String typemessage152 = getEventStatuses145mtemp.getMessage();
        String tempResultmessage152 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage152));
%>
        <%=tempResultmessage152%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 158:
        gotMethod = true;
        String sessionId_25id=  request.getParameter("sessionId169");
            java.lang.String sessionId_25idTemp = null;
        if(!sessionId_25id.equals("")){
         sessionId_25idTemp  = sessionId_25id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1ExistingSessionRequest_24id" scope="session" class="com.lytx.dto.ExistingSessionRequest" />
        <%
        	com1DriveCam1Classes1ExistingSessionRequest_24id.setSessionId(sessionId_25idTemp);
                com.lytx.dto.GetBehaviorsResponse getBehaviors158mtemp = sampleISubmissionServiceV5Proxyid.getBehaviors(com1DriveCam1Classes1ExistingSessionRequest_24id);
        if(getBehaviors158mtemp == null){
        %>
<%=getBehaviors158mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">behaviors:</TD>
<TD>
<%
	if(getBehaviors158mtemp != null){
com.lytx.dto.Behavior[] typebehaviors161 = getBehaviors158mtemp.getBehaviors();
        String tempbehaviors161 = null;
        if(typebehaviors161 != null){
        java.util.List listbehaviors161= java.util.Arrays.asList(typebehaviors161);
        tempbehaviors161 = listbehaviors161.toString();
        }
%>
        <%=tempbehaviors161%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getBehaviors158mtemp != null){
java.lang.Boolean typeerror163 = getBehaviors158mtemp.getError();
        String tempResulterror163 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror163));
%>
        <%=tempResulterror163%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getBehaviors158mtemp != null){
java.lang.String typemessage165 = getBehaviors158mtemp.getMessage();
        String tempResultmessage165 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage165));
%>
        <%=tempResultmessage165%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 171:
        gotMethod = true;
        String includeSubgroups_27id=  request.getParameter("includeSubgroups182");
            java.lang.Boolean includeSubgroups_27idTemp = null;
        if(!includeSubgroups_27id.equals("")){
         includeSubgroups_27idTemp  = java.lang.Boolean.valueOf(includeSubgroups_27id);
        }
        String groupId_28id=  request.getParameter("groupId184");
            java.lang.Long groupId_28idTemp = null;
        if(!groupId_28id.equals("")){
         groupId_28idTemp  = java.lang.Long.valueOf(groupId_28id);
        }
        String sessionId_29id=  request.getParameter("sessionId186");
            java.lang.String sessionId_29idTemp = null;
        if(!sessionId_29id.equals("")){
         sessionId_29idTemp  = sessionId_29id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1GetUsersRequest_26id" scope="session" class="com.lytx.dto.GetUsersRequest" />
        <%
        	com1DriveCam1Classes1GetUsersRequest_26id.setIncludeSubgroups(includeSubgroups_27idTemp);
                com1DriveCam1Classes1GetUsersRequest_26id.setGroupId(groupId_28idTemp);
                com1DriveCam1Classes1GetUsersRequest_26id.setSessionId(sessionId_29idTemp);
                com.lytx.dto.GetUsersResponse getUsers171mtemp = sampleISubmissionServiceV5Proxyid.getUsers(com1DriveCam1Classes1GetUsersRequest_26id);
        if(getUsers171mtemp == null){
        %>
<%=getUsers171mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getUsers171mtemp != null){
java.lang.Boolean typeerror174 = getUsers171mtemp.getError();
        String tempResulterror174 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror174));
%>
        <%=tempResulterror174%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getUsers171mtemp != null){
java.lang.String typemessage176 = getUsers171mtemp.getMessage();
        String tempResultmessage176 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage176));
%>
        <%=tempResultmessage176%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">users:</TD>
<TD>
<%
	if(getUsers171mtemp != null){
com.lytx.dto.UserInfo[] typeusers178 = getUsers171mtemp.getUsers();
        String tempusers178 = null;
        if(typeusers178 != null){
        java.util.List listusers178= java.util.Arrays.asList(typeusers178);
        tempusers178 = listusers178.toString();
        }
%>
        <%=tempusers178%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 188:
        gotMethod = true;
        String includeSubgroups_31id=  request.getParameter("includeSubgroups199");
            java.lang.Boolean includeSubgroups_31idTemp = null;
        if(!includeSubgroups_31id.equals("")){
         includeSubgroups_31idTemp  = java.lang.Boolean.valueOf(includeSubgroups_31id);
        }
        String groupId_32id=  request.getParameter("groupId201");
            java.lang.Long groupId_32idTemp = null;
        if(!groupId_32id.equals("")){
         groupId_32idTemp  = java.lang.Long.valueOf(groupId_32id);
        }
        String sessionId_33id=  request.getParameter("sessionId203");
            java.lang.String sessionId_33idTemp = null;
        if(!sessionId_33id.equals("")){
         sessionId_33idTemp  = sessionId_33id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1GetGroupsByIdRequest_30id" scope="session" class="com.lytx.dto.GetGroupsByIdRequest" />
        <%
        	com1DriveCam1Classes1GetGroupsByIdRequest_30id.setIncludeSubgroups(includeSubgroups_31idTemp);
                com1DriveCam1Classes1GetGroupsByIdRequest_30id.setGroupId(groupId_32idTemp);
                com1DriveCam1Classes1GetGroupsByIdRequest_30id.setSessionId(sessionId_33idTemp);
                com.lytx.dto.GetGroupsResponse getGroupsById188mtemp = sampleISubmissionServiceV5Proxyid.getGroupsById(com1DriveCam1Classes1GetGroupsByIdRequest_30id);
        if(getGroupsById188mtemp == null){
        %>
<%=getGroupsById188mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">groups:</TD>
<TD>
<%
	if(getGroupsById188mtemp != null){
com.lytx.dto.GroupInfo[] typegroups191 = getGroupsById188mtemp.getGroups();
        String tempgroups191 = null;
        if(typegroups191 != null){
        java.util.List listgroups191= java.util.Arrays.asList(typegroups191);
        tempgroups191 = listgroups191.toString();
        }
%>
        <%=tempgroups191%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getGroupsById188mtemp != null){
java.lang.Boolean typeerror193 = getGroupsById188mtemp.getError();
        String tempResulterror193 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror193));
%>
        <%=tempResulterror193%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getGroupsById188mtemp != null){
java.lang.String typemessage195 = getGroupsById188mtemp.getMessage();
        String tempResultmessage195 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage195));
%>
        <%=tempResultmessage195%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 205:
        gotMethod = true;
        String includeSubgroups_35id=  request.getParameter("includeSubgroups216");
            java.lang.Boolean includeSubgroups_35idTemp = null;
        if(!includeSubgroups_35id.equals("")){
         includeSubgroups_35idTemp  = java.lang.Boolean.valueOf(includeSubgroups_35id);
        }
        String path_36id=  request.getParameter("path218");
            java.lang.String path_36idTemp = null;
        if(!path_36id.equals("")){
         path_36idTemp  = path_36id;
        }
        String sessionId_37id=  request.getParameter("sessionId220");
            java.lang.String sessionId_37idTemp = null;
        if(!sessionId_37id.equals("")){
         sessionId_37idTemp  = sessionId_37id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1GetGroupsByFullPathRequest_34id" scope="session" class="com.lytx.dto.GetGroupsByFullPathRequest" />
        <%
        	com1DriveCam1Classes1GetGroupsByFullPathRequest_34id.setIncludeSubgroups(includeSubgroups_35idTemp);
                com1DriveCam1Classes1GetGroupsByFullPathRequest_34id.setPath(path_36idTemp);
                com1DriveCam1Classes1GetGroupsByFullPathRequest_34id.setSessionId(sessionId_37idTemp);
                com.lytx.dto.GetGroupsResponse getGroupsByPath205mtemp = sampleISubmissionServiceV5Proxyid.getGroupsByPath(com1DriveCam1Classes1GetGroupsByFullPathRequest_34id);
        if(getGroupsByPath205mtemp == null){
        %>
<%=getGroupsByPath205mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">groups:</TD>
<TD>
<%
	if(getGroupsByPath205mtemp != null){
com.lytx.dto.GroupInfo[] typegroups208 = getGroupsByPath205mtemp.getGroups();
        String tempgroups208 = null;
        if(typegroups208 != null){
        java.util.List listgroups208= java.util.Arrays.asList(typegroups208);
        tempgroups208 = listgroups208.toString();
        }
%>
        <%=tempgroups208%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getGroupsByPath205mtemp != null){
java.lang.Boolean typeerror210 = getGroupsByPath205mtemp.getError();
        String tempResulterror210 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror210));
%>
        <%=tempResulterror210%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
	if(getGroupsByPath205mtemp != null){
java.lang.String typemessage212 = getGroupsByPath205mtemp.getMessage();
        String tempResultmessage212 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage212));
%>
        <%=tempResultmessage212%>
        <%
        	}
        %>
</TD>
</TABLE>
<%
	}
break;
case 222:
        gotMethod = true;
        String startDate_39id=  request.getParameter("startDate235");
            java.util.Calendar startDate_39idTemp = null;
        if(!startDate_39id.equals("")){
        java.text.DateFormat dateFormatstartDate235 = java.text.DateFormat.getDateInstance();
        java.util.Date dateTempstartDate235  = dateFormatstartDate235.parse(startDate_39id);
         startDate_39idTemp = new java.util.GregorianCalendar();
        startDate_39idTemp.setTime(dateTempstartDate235);
        }
        String includeSubgroups_40id=  request.getParameter("includeSubgroups237");
            java.lang.Boolean includeSubgroups_40idTemp = null;
        if(!includeSubgroups_40id.equals("")){
         includeSubgroups_40idTemp  = java.lang.Boolean.valueOf(includeSubgroups_40id);
        }
        String groupId_41id=  request.getParameter("groupId239");
            java.lang.Long groupId_41idTemp = null;
        if(!groupId_41id.equals("")){
         groupId_41idTemp  = java.lang.Long.valueOf(groupId_41id);
        }
        String endDate_42id=  request.getParameter("endDate241");
            java.util.Calendar endDate_42idTemp = null;
        if(!endDate_42id.equals("")){
        java.text.DateFormat dateFormatendDate241 = java.text.DateFormat.getDateInstance();
        java.util.Date dateTempendDate241  = dateFormatendDate241.parse(endDate_42id);
         endDate_42idTemp = new java.util.GregorianCalendar();
        endDate_42idTemp.setTime(dateTempendDate241);
        }
        String sessionId_43id=  request.getParameter("sessionId243");
            java.lang.String sessionId_43idTemp = null;
        if(!sessionId_43id.equals("")){
         sessionId_43idTemp  = sessionId_43id;
        }
%>
        <jsp:useBean id="com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id" scope="session" class="com.lytx.dto.GetEventsByLastUpdateDateRequest" />
        <%
        	com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id.setStartDate(startDate_39idTemp);
                com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id.setIncludeSubgroups(includeSubgroups_40idTemp);
                com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id.setGroupId(groupId_41idTemp);
                com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id.setEndDate(endDate_42idTemp);
                com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id.setSessionId(sessionId_43idTemp);
                org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse getEventsByLastUpdateDate222mtemp = sampleISubmissionServiceV5Proxyid.getEventsByLastUpdateDate(com1DriveCam1Classes1GetEventsByLastUpdateDateRequest_38id);
        if(getEventsByLastUpdateDate222mtemp == null){
        %>
<%=getEventsByLastUpdateDate222mtemp%>
<%
	}else{
%>
<TABLE>
<TR>
<TD COLSPAN="3" ALIGN="LEFT">returnp:</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">error:</TD>
<TD>
<%
	if(getEventsByLastUpdateDate222mtemp != null){
java.lang.Boolean typeerror225 = getEventsByLastUpdateDate222mtemp.getError();
        String tempResulterror225 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typeerror225));
%>
        <%=tempResulterror225%>
        <%
        	}
        %>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">events:</TD>
<TD>
<%
	if(getEventsByLastUpdateDate222mtemp != null){
com.lytx.dto.EventsInfoV5[] typeevents227 = getEventsByLastUpdateDate222mtemp.getEvents();
        String tempevents227 = null;
        if(typeevents227 != null){
        java.util.List listevents227= java.util.Arrays.asList(typeevents227);
        tempevents227 = listevents227.toString();
        }
%>
        <%=tempevents227%>
        <%
}%>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">message:</TD>
<TD>
<%
if(getEventsByLastUpdateDate222mtemp != null){
java.lang.String typemessage229 = getEventsByLastUpdateDate222mtemp.getMessage();
        String tempResultmessage229 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(typemessage229));
        %>
        <%= tempResultmessage229 %>
        <%
}%>
</TD>
<TR>
<TD WIDTH="5%"></TD>
<TD COLSPAN="2" ALIGN="LEFT">queryCutoff:</TD>
<TD>
<%
if(getEventsByLastUpdateDate222mtemp != null){
java.util.Calendar typequeryCutoff231 = getEventsByLastUpdateDate222mtemp.getQueryCutoff();
        java.text.DateFormat dateFormatqueryCutoff231 = java.text.DateFormat.getDateInstance();
        java.util.Date datequeryCutoff231 = typequeryCutoff231.getTime();
        String tempResultqueryCutoff231 = org.eclipse.jst.ws.util.JspUtils.markup(dateFormatqueryCutoff231.format(datequeryCutoff231));
        %>
        <%= tempResultqueryCutoff231 %>
        <%
}%>
</TD>
</TABLE>
<%
}
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>