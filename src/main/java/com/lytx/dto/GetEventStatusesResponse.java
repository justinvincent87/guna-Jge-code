/**
 * GetEventStatusesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class GetEventStatusesResponse implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.Boolean error;

	private com.lytx.dto.EventStatus[] eventStatuses;

	private java.lang.String message;

	public GetEventStatusesResponse() {
	}

	public GetEventStatusesResponse(java.lang.Boolean error, com.lytx.dto.EventStatus[] eventStatuses,
	        java.lang.String message) {
		this.error			= error;
		this.eventStatuses	= eventStatuses;
		this.message		= message;
	}

	/**
	 * Gets the error value for this GetEventStatusesResponse.
	 * 
	 * @return error
	 */
	public java.lang.Boolean getError() {
		return error;
	}

	/**
	 * Sets the error value for this GetEventStatusesResponse.
	 * 
	 * @param error
	 */
	public void setError(java.lang.Boolean error) {
		this.error = error;
	}

	/**
	 * Gets the eventStatuses value for this GetEventStatusesResponse.
	 * 
	 * @return eventStatuses
	 */
	public com.lytx.dto.EventStatus[] getEventStatuses() {
		return eventStatuses;
	}

	/**
	 * Sets the eventStatuses value for this GetEventStatusesResponse.
	 * 
	 * @param eventStatuses
	 */
	public void setEventStatuses(com.lytx.dto.EventStatus[] eventStatuses) {
		this.eventStatuses = eventStatuses;
	}

	/**
	 * Gets the message value for this GetEventStatusesResponse.
	 * 
	 * @return message
	 */
	public java.lang.String getMessage() {
		return message;
	}

	/**
	 * Sets the message value for this GetEventStatusesResponse.
	 * 
	 * @param message
	 */
	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetEventStatusesResponse))
			return false;
		GetEventStatusesResponse other = (GetEventStatusesResponse) obj;
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
		        && ((this.eventStatuses == null && other.getEventStatuses() == null) || (this.eventStatuses != null
		                && java.util.Arrays.equals(this.eventStatuses, other.getEventStatuses())))
		        && ((this.message == null && other.getMessage() == null)
		                || (this.message != null && this.message.equals(other.getMessage())));
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
		if (getEventStatuses() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getEventStatuses()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getEventStatuses(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getMessage() != null) {
			_hashCode += getMessage().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        GetEventStatusesResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GetEventStatusesResponse"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("error");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Error"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("eventStatuses");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EventStatuses"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EventStatus"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EventStatus"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("message");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Message"));
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
