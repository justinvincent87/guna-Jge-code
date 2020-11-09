/**
 * EventsInfoV3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class EventsInfoV3 extends com.lytx.dto.EventsInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String eventLink;

	public EventsInfoV3() {
	}

	public EventsInfoV3(
	        org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior[] behaviors,
	        java.lang.Long coachId, java.util.Calendar creationDate, java.lang.Long customerEventId,
	        java.lang.String customerEventIdString, java.util.Calendar downloadedDate, java.lang.Long driverId,
	        java.lang.String ERSerialNumber, java.lang.Long eventId, java.lang.Long eventStatusId,
	        java.lang.Long eventTypeId, java.math.BigDecimal forwardMax, java.math.BigDecimal forwardThreshold,
	        java.lang.Long groupId, java.math.BigDecimal heading, java.util.Calendar lastUpdated,
	        java.math.BigDecimal lateralMax, java.math.BigDecimal lateralThreshold, java.math.BigDecimal latitude,
	        java.math.BigDecimal longitude, java.lang.String notes, java.lang.Integer objectRevision,
	        java.math.BigDecimal overdue, java.lang.String recordDateTZ, java.util.Calendar recordDateUTC,
	        java.lang.Integer recordDateUtcOffset, java.util.Calendar reviewedDate, java.util.Calendar revisionDate,
	        java.lang.Long score, java.math.BigDecimal shockThreshold, java.math.BigDecimal speed,
	        java.lang.Long vehicleId, java.lang.String eventLink) {
		super(behaviors, coachId, creationDate, customerEventId, customerEventIdString, downloadedDate, driverId,
		        ERSerialNumber, eventId, eventStatusId, eventTypeId, forwardMax, forwardThreshold, groupId, heading,
		        lastUpdated, lateralMax, lateralThreshold, latitude, longitude, notes, objectRevision, overdue,
		        recordDateTZ, recordDateUTC, recordDateUtcOffset, reviewedDate, revisionDate, score, shockThreshold,
		        speed, vehicleId);
		this.eventLink = eventLink;
	}

	/**
	 * Gets the eventLink value for this EventsInfoV3.
	 * 
	 * @return eventLink
	 */
	public java.lang.String getEventLink() {
		return eventLink;
	}

	/**
	 * Sets the eventLink value for this EventsInfoV3.
	 * 
	 * @param eventLink
	 */
	public void setEventLink(java.lang.String eventLink) {
		this.eventLink = eventLink;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof EventsInfoV3))
			return false;
		EventsInfoV3 other = (EventsInfoV3) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals			= super.equals(obj) && ((this.eventLink == null && other.getEventLink() == null)
		        || (this.eventLink != null && this.eventLink.equals(other.getEventLink())));
		__equalsCalc	= null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = super.hashCode();
		if (getEventLink() != null) {
			_hashCode += getEventLink().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        EventsInfoV3.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfoV3"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("eventLink");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventLink"));
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
