/**
 * GetVehicleTypesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class GetVehicleTypesResponse implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.Boolean error;

	private java.lang.String message;

	private com.lytx.dto.VehicleType[] vehicleTypes;

	public GetVehicleTypesResponse() {
	}

	public GetVehicleTypesResponse(java.lang.Boolean error, java.lang.String message,
	        com.lytx.dto.VehicleType[] vehicleTypes) {
		this.error			= error;
		this.message		= message;
		this.vehicleTypes	= vehicleTypes;
	}

	/**
	 * Gets the error value for this GetVehicleTypesResponse.
	 * 
	 * @return error
	 */
	public java.lang.Boolean getError() {
		return error;
	}

	/**
	 * Sets the error value for this GetVehicleTypesResponse.
	 * 
	 * @param error
	 */
	public void setError(java.lang.Boolean error) {
		this.error = error;
	}

	/**
	 * Gets the message value for this GetVehicleTypesResponse.
	 * 
	 * @return message
	 */
	public java.lang.String getMessage() {
		return message;
	}

	/**
	 * Sets the message value for this GetVehicleTypesResponse.
	 * 
	 * @param message
	 */
	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	/**
	 * Gets the vehicleTypes value for this GetVehicleTypesResponse.
	 * 
	 * @return vehicleTypes
	 */
	public com.lytx.dto.VehicleType[] getVehicleTypes() {
		return vehicleTypes;
	}

	/**
	 * Sets the vehicleTypes value for this GetVehicleTypesResponse.
	 * 
	 * @param vehicleTypes
	 */
	public void setVehicleTypes(com.lytx.dto.VehicleType[] vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GetVehicleTypesResponse))
			return false;
		GetVehicleTypesResponse other = (GetVehicleTypesResponse) obj;
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
		        && ((this.vehicleTypes == null && other.getVehicleTypes() == null) || (this.vehicleTypes != null
		                && java.util.Arrays.equals(this.vehicleTypes, other.getVehicleTypes())));
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
		if (getVehicleTypes() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getVehicleTypes()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getVehicleTypes(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        GetVehicleTypesResponse.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GetVehicleTypesResponse"));
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
		elemField.setFieldName("vehicleTypes");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleTypes"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleType"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleType"));
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
