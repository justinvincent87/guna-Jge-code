/**
 * SubmissionServiceV5Locator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.services.impl;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SubmissionServiceV5Locator extends org.apache.axis.client.Service
        implements com.lytx.services.impl.SubmissionServiceV5 {

	private static final long serialVersionUID = 1L;

	public SubmissionServiceV5Locator() {
	}

	public SubmissionServiceV5Locator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public SubmissionServiceV5Locator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
	        throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for BasicHttpBinding_ISubmissionServiceV5
	private java.lang.String BasicHttpBinding_ISubmissionServiceV5_address = "https://services-sd02.drivecam.com/DCSubmission/SubmissionServiceV5.svc";

	// private java.lang.String BasicHttpBinding_ISubmissionServiceV5_address06 =
	// "https://services-sd06.drivecam.com/DCSubmission/SubmissionServiceV5.svc";

	public java.lang.String getBasicHttpBinding_ISubmissionServiceV5Address() {
		return BasicHttpBinding_ISubmissionServiceV5_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String BasicHttpBinding_ISubmissionServiceV5WSDDServiceName = "BasicHttpBinding_ISubmissionServiceV5";

	public java.lang.String getBasicHttpBinding_ISubmissionServiceV5WSDDServiceName() {
		return BasicHttpBinding_ISubmissionServiceV5WSDDServiceName;
	}

	public void setBasicHttpBinding_ISubmissionServiceV5WSDDServiceName(java.lang.String name) {
		BasicHttpBinding_ISubmissionServiceV5WSDDServiceName = name;
	}

	public com.lytx.services.ISubmissionServiceV5 getBasicHttpBinding_ISubmissionServiceV5()
	        throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(BasicHttpBinding_ISubmissionServiceV5_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getBasicHttpBinding_ISubmissionServiceV5(endpoint);
	}

	public com.lytx.services.ISubmissionServiceV5 getBasicHttpBinding_ISubmissionServiceV5(java.net.URL portAddress)
	        throws javax.xml.rpc.ServiceException {
		try {
			com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub _stub = new com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub(
			        portAddress, this);
			_stub.setPortName(getBasicHttpBinding_ISubmissionServiceV5WSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public com.lytx.services.ISubmissionServiceV5 getBasicHttpBinding_ISubmissionServiceV506(java.net.URL portAddress)
	        throws javax.xml.rpc.ServiceException {
		try {
			com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub _stub = new com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub(
			        portAddress, this);
			_stub.setPortName(getBasicHttpBinding_ISubmissionServiceV5WSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setBasicHttpBinding_ISubmissionServiceV5EndpointAddress(java.lang.String address) {
		BasicHttpBinding_ISubmissionServiceV5_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no
	 * port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (com.lytx.services.ISubmissionServiceV5.class.isAssignableFrom(serviceEndpointInterface)) {
				com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub _stub = new com.lytx.services.impl.BasicHttpBinding_ISubmissionServiceV5Stub(
				        new java.net.URL(BasicHttpBinding_ISubmissionServiceV5_address), this);
				_stub.setPortName(getBasicHttpBinding_ISubmissionServiceV5WSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
		        + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has no
	 * port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
	        throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("BasicHttpBinding_ISubmissionServiceV5".equals(inputPortName)) {
			return getBasicHttpBinding_ISubmissionServiceV5();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://DriveCam.com/Services", "SubmissionServiceV5");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://DriveCam.com/Services",
			        "BasicHttpBinding_ISubmissionServiceV5"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(java.lang.String portName, java.lang.String address)
	        throws javax.xml.rpc.ServiceException {

		if ("BasicHttpBinding_ISubmissionServiceV5".equals(portName)) {
			setBasicHttpBinding_ISubmissionServiceV5EndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
	        throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
