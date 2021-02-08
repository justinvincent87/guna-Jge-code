/**
 * EventsInfoV5.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class EventsInfoV5  extends com.lytx.dto.EventsInfoV4  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private java.lang.String eventLink2;

    public EventsInfoV5() {
    }

    public EventsInfoV5(
           org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior[] behaviors,
           java.lang.Long coachId,
           java.util.Calendar creationDate,
           java.lang.Long customerEventId,
           java.lang.String customerEventIdString,
           java.util.Calendar downloadedDate,
           java.lang.Long driverId,
           java.lang.String ERSerialNumber,
           java.lang.Long eventId,
           java.lang.Long eventStatusId,
           java.lang.Long eventTypeId,
           java.math.BigDecimal forwardMax,
           java.math.BigDecimal forwardThreshold,
           java.lang.Long groupId,
           java.math.BigDecimal heading,
           java.util.Calendar lastUpdated,
           java.math.BigDecimal lateralMax,
           java.math.BigDecimal lateralThreshold,
           java.math.BigDecimal latitude,
           java.math.BigDecimal longitude,
           java.lang.String notes,
           java.lang.Integer objectRevision,
           java.math.BigDecimal overdue,
           java.lang.String recordDateTZ,
           java.util.Calendar recordDateUTC,
           java.lang.Integer recordDateUtcOffset,
           java.util.Calendar reviewedDate,
           java.util.Calendar revisionDate,
           java.lang.Long score,
           java.math.BigDecimal shockThreshold,
           java.math.BigDecimal speed,
           java.lang.Long vehicleId,
           java.lang.String eventLink,
           com.lytx.dto.NoteInfo[] eventNotes,
           java.lang.Boolean isCoachable,
           com.lytx.dto.NoteInfo[] reviewNotes,
           com.lytx.dto.NoteInfo[] sessionNotes,
           java.lang.String eventLink2) {
        super(
            behaviors,
            coachId,
            creationDate,
            customerEventId,
            customerEventIdString,
            downloadedDate,
            driverId,
            ERSerialNumber,
            eventId,
            eventStatusId,
            eventTypeId,
            forwardMax,
            forwardThreshold,
            groupId,
            heading,
            lastUpdated,
            lateralMax,
            lateralThreshold,
            latitude,
            longitude,
            notes,
            objectRevision,
            overdue,
            recordDateTZ,
            recordDateUTC,
            recordDateUtcOffset,
            reviewedDate,
            revisionDate,
            score,
            shockThreshold,
            speed,
            vehicleId,
            eventLink,
            eventNotes,
            isCoachable,
            reviewNotes,
            sessionNotes);
        this.eventLink2 = eventLink2;
    }


    /**
     * Gets the eventLink2 value for this EventsInfoV5.
     * 
     * @return eventLink2
     */
    public java.lang.String getEventLink2() {
        return eventLink2;
    }


    /**
     * Sets the eventLink2 value for this EventsInfoV5.
     * 
     * @param eventLink2
     */
    public void setEventLink2(java.lang.String eventLink2) {
        this.eventLink2 = eventLink2;
    }

    private java.lang.Object __equalsCalc = null;
    @SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventsInfoV5)) return false;
        EventsInfoV5 other = (EventsInfoV5) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.eventLink2==null && other.getEventLink2()==null) || 
             (this.eventLink2!=null &&
              this.eventLink2.equals(other.getEventLink2())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getEventLink2() != null) {
            _hashCode += getEventLink2().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventsInfoV5.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfoV5"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventLink2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventLink2"));
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
	public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    @SuppressWarnings("rawtypes")
	public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
	        java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
