/**
 * SubmitDriverDispatchLogsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class SubmitDriverDispatchLogsResponse implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.Boolean	error;
	private java.lang.String	message;
	private java.lang.Long		submissionId;
	private java.lang.String	validationResponse;

	public SubmitDriverDispatchLogsResponse() {
	}

	public SubmitDriverDispatchLogsResponse(java.lang.Boolean error, java.lang.String message,
	        java.lang.Long submissionId, java.lang.String validationResponse) {
		this.error				= error;
		this.message			= message;
		this.submissionId		= submissionId;
		this.validationResponse	= validationResponse;
	}

	/**
	 * Gets the error value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @return error
	 */
	public java.lang.Boolean getError() {
		return error;
	}

	/**
	 * Sets the error value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @param error
	 */
	public void setError(java.lang.Boolean error) {
		this.error = error;
	}

	/**
	 * Gets the message value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @return message
	 */
	public java.lang.String getMessage() {
		return message;
	}

	/**
	 * Sets the message value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @param message
	 */
	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	/**
	 * Gets the submissionId value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @return submissionId
	 */
	public java.lang.Long getSubmissionId() {
		return submissionId;
	}

	/**
	 * Sets the submissionId value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @param submissionId
	 */
	public void setSubmissionId(java.lang.Long submissionId) {
		this.submissionId = submissionId;
	}

	/**
	 * Gets the validationResponse value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @return validationResponse
	 */
	public java.lang.String getValidationResponse() {
		return validationResponse;
	}

	/**
	 * Sets the validationResponse value for this SubmitDriverDispatchLogsResponse.
	 * 
	 * @param validationResponse
	 */
	public void setValidationResponse(java.lang.String validationResponse) {
		this.validationResponse = validationResponse;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SubmitDriverDispatchLogsResponse))
			return false;
		SubmitDriverDispatchLogsResponse other = (SubmitDriverDispatchLogsResponse) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals			= true
		        && ((this.error == null && other.getError() == null)
		                || (this.error != null && this.error.equals(other.getError())))
		        && ((this.message == null && other.getMessage() == null)
		                || (this.message != null && this.message.equals(other.getMessage())))
		        && ((this.submissionId == null && other.getSubmissionId() == null)
		                || (this.submissionId != null && this.submissionId.equals(other.getSubmissionId())))
		        && ((this.validationResponse == null && other.getValidationResponse() == null)
		                || (this.validationResponse != null
		                        && this.validationResponse.equals(other.getValidationResponse())));
		__equalsCalc	= null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getError() != null) {
			_hashCode += getError().hashCode();
		}
		if (getMessage() != null) {
			_hashCode += getMessage().hashCode();
		}
		if (getSubmissionId() != null) {
			_hashCode += getSubmissionId().hashCode();
		}
		if (getValidationResponse() != null) {
			_hashCode += getValidationResponse().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        SubmitDriverDispatchLogsResponse.class, true);

	static {
		typeDesc.setXmlType(
		        new javax.xml.namespace.QName("http://DriveCam.com/Classes", "SubmitDriverDispatchLogsResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("error");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Error"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("message");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Message"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("submissionId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "SubmissionId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("validationResponse");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "ValidationResponse"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
	        java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	@SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
	        java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
