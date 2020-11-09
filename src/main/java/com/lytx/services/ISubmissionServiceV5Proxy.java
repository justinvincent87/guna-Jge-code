package com.lytx.services;

public class ISubmissionServiceV5Proxy implements com.lytx.services.ISubmissionServiceV5 {
	private String									_endpoint				= null;
	private com.lytx.services.ISubmissionServiceV5	iSubmissionServiceV5	= null;

	public ISubmissionServiceV5Proxy() {
		_initISubmissionServiceV5Proxy();
	}

	public ISubmissionServiceV5Proxy(String endpoint) {
		_endpoint = endpoint;
		_initISubmissionServiceV5Proxy();
	}

	private void _initISubmissionServiceV5Proxy() {
		try {
			iSubmissionServiceV5 = (new com.lytx.services.impl.SubmissionServiceV5Locator())
			        .getBasicHttpBinding_ISubmissionServiceV5();
			if (iSubmissionServiceV5 != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) iSubmissionServiceV5)._setProperty("javax.xml.rpc.service.endpoint.address",
					        _endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) iSubmissionServiceV5)
					        ._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}

	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (iSubmissionServiceV5 != null)
			((javax.xml.rpc.Stub) iSubmissionServiceV5)._setProperty("javax.xml.rpc.service.endpoint.address",
			        _endpoint);

	}

	public com.lytx.services.ISubmissionServiceV5 getISubmissionServiceV5() {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5;
	}

	public java.lang.String ping() throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.ping();
	}

	public com.lytx.dto.LoginResponse login(java.lang.String userName, java.lang.String password)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.login(userName, password);
	}

	public com.lytx.dto.LoginResponse loginCompany(java.lang.String userName, java.lang.String password,
	        java.lang.Long companyId) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.loginCompany(userName, password, companyId);
	}

	public com.lytx.dto.SubmitDriverDispatchLogsResponse submitDriverDispatchLogs(java.lang.String sessionId,
	        java.lang.String dispatchEntries) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.submitDriverDispatchLogs(sessionId, dispatchEntries);
	}

	public com.lytx.dto.GetSubmissionStatusResponse getSubmissionStatus(java.lang.String sessionId,
	        java.lang.Long submissionId) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getSubmissionStatus(sessionId, submissionId);
	}

	public com.lytx.dto.GetSubmissionResultsResponse getSubmissionResults(java.lang.String sessionId,
	        java.lang.Long submissionId) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getSubmissionResults(sessionId, submissionId);
	}

	public com.lytx.dto.GetVehiclesResponse getVehicles(com.lytx.dto.GetVehiclesRequest getVehiclesRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getVehicles(getVehiclesRequest);
	}

	public com.lytx.dto.GetVehicleTypesResponse getVehicleTypes(
	        com.lytx.dto.ExistingSessionRequest getVehicleTypesRequest) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getVehicleTypes(getVehicleTypesRequest);
	}

	public com.lytx.dto.GetVehicleStatusesResponse getVehicleStatuses(
	        com.lytx.dto.ExistingSessionRequest getVehicleStatusesRequest) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getVehicleStatuses(getVehicleStatusesRequest);
	}

	public com.lytx.dto.GetEventTypesResponse getEventTypes(com.lytx.dto.ExistingSessionRequest getEventTypesRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getEventTypes(getEventTypesRequest);
	}

	public com.lytx.dto.GetEventStatusesResponse getEventStatuses(
	        com.lytx.dto.ExistingSessionRequest getEventStatusesRequest) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getEventStatuses(getEventStatusesRequest);
	}

	public com.lytx.dto.GetBehaviorsResponse getBehaviors(com.lytx.dto.ExistingSessionRequest getBehaviorsRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getBehaviors(getBehaviorsRequest);
	}

	public com.lytx.dto.GetUsersResponse getUsers(com.lytx.dto.GetUsersRequest getUsersRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getUsers(getUsersRequest);
	}

	public com.lytx.dto.GetGroupsResponse getGroupsById(com.lytx.dto.GetGroupsByIdRequest getGroupsByIdRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getGroupsById(getGroupsByIdRequest);
	}

	public com.lytx.dto.GetGroupsResponse getGroupsByPath(
	        com.lytx.dto.GetGroupsByFullPathRequest getGroupsByFullPathRequest) throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getGroupsByPath(getGroupsByFullPathRequest);
	}

	public org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse getEventsByLastUpdateDate(
	        com.lytx.dto.GetEventsByLastUpdateDateRequest getEventsByLastUpdateDateRequest)
	        throws java.rmi.RemoteException {
		if (iSubmissionServiceV5 == null)
			_initISubmissionServiceV5Proxy();
		return iSubmissionServiceV5.getEventsByLastUpdateDate(getEventsByLastUpdateDateRequest);
	}

}