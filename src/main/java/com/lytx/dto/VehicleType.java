/**
 * VehicleType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class VehicleType implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.util.Calendar	creationDate;
	private java.lang.String	description;
	private java.lang.Long		vehicleTypeId;

	public VehicleType() {
	}

	public VehicleType(java.util.Calendar creationDate, java.lang.String description, java.lang.Long vehicleTypeId) {
		this.creationDate	= creationDate;
		this.description	= description;
		this.vehicleTypeId	= vehicleTypeId;
	}

	/**
	 * Gets the creationDate value for this VehicleType.
	 * 
	 * @return creationDate
	 */
	public java.util.Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creationDate value for this VehicleType.
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(java.util.Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the description value for this VehicleType.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this VehicleType.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the vehicleTypeId value for this VehicleType.
	 * 
	 * @return vehicleTypeId
	 */
	public java.lang.Long getVehicleTypeId() {
		return vehicleTypeId;
	}

	/**
	 * Sets the vehicleTypeId value for this VehicleType.
	 * 
	 * @param vehicleTypeId
	 */
	public void setVehicleTypeId(java.lang.Long vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof VehicleType))
			return false;
		VehicleType other = (VehicleType) obj;
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
		        && ((this.creationDate == null && other.getCreationDate() == null)
		                || (this.creationDate != null && this.creationDate.equals(other.getCreationDate())))
		        && ((this.description == null && other.getDescription() == null)
		                || (this.description != null && this.description.equals(other.getDescription())))
		        && ((this.vehicleTypeId == null && other.getVehicleTypeId() == null)
		                || (this.vehicleTypeId != null && this.vehicleTypeId.equals(other.getVehicleTypeId())));
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
		if (getCreationDate() != null) {
			_hashCode += getCreationDate().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		if (getVehicleTypeId() != null) {
			_hashCode += getVehicleTypeId().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        VehicleType.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleType"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("creationDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "CreationDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Description"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("vehicleTypeId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleTypeId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
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
