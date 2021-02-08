/**
 * ISubmissionServiceV5.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.services;

public interface ISubmissionServiceV5 extends java.rmi.Remote {
	public java.lang.String ping() throws java.rmi.RemoteException;

	public com.lytx.dto.LoginResponse login(java.lang.String userName, java.lang.String password)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.LoginResponse loginCompany(java.lang.String userName, java.lang.String password,
	        java.lang.Long companyId) throws java.rmi.RemoteException;

	public com.lytx.dto.SubmitDriverDispatchLogsResponse submitDriverDispatchLogs(java.lang.String sessionId,
	        java.lang.String dispatchEntries) throws java.rmi.RemoteException;

	public com.lytx.dto.GetSubmissionStatusResponse getSubmissionStatus(java.lang.String sessionId,
	        java.lang.Long submissionId) throws java.rmi.RemoteException;

	public com.lytx.dto.GetSubmissionResultsResponse getSubmissionResults(java.lang.String sessionId,
	        java.lang.Long submissionId) throws java.rmi.RemoteException;

	public com.lytx.dto.GetVehiclesResponse getVehicles(com.lytx.dto.GetVehiclesRequest getVehiclesRequest)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.GetVehicleTypesResponse getVehicleTypes(
	        com.lytx.dto.ExistingSessionRequest getVehicleTypesRequest) throws java.rmi.RemoteException;

	public com.lytx.dto.GetVehicleStatusesResponse getVehicleStatuses(
	        com.lytx.dto.ExistingSessionRequest getVehicleStatusesRequest) throws java.rmi.RemoteException;

	public com.lytx.dto.GetEventTypesResponse getEventTypes(com.lytx.dto.ExistingSessionRequest getEventTypesRequest)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.GetEventStatusesResponse getEventStatuses(
	        com.lytx.dto.ExistingSessionRequest getEventStatusesRequest) throws java.rmi.RemoteException;

	public com.lytx.dto.GetBehaviorsResponse getBehaviors(com.lytx.dto.ExistingSessionRequest getBehaviorsRequest)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.GetUsersResponse getUsers(com.lytx.dto.GetUsersRequest getUsersRequest)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.GetGroupsResponse getGroupsById(com.lytx.dto.GetGroupsByIdRequest getGroupsByIdRequest)
	        throws java.rmi.RemoteException;

	public com.lytx.dto.GetGroupsResponse getGroupsByPath(
	        com.lytx.dto.GetGroupsByFullPathRequest getGroupsByFullPathRequest) throws java.rmi.RemoteException;

	public org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5.GetEventsResponse getEventsByLastUpdateDate(
	        com.lytx.dto.GetEventsByLastUpdateDateRequest getEventsByLastUpdateDateRequest)
	        throws java.rmi.RemoteException;
}
