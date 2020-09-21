/**
 * EventsInfoV4.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class EventsInfoV4  extends com.lytx.dto.EventsInfoV3  implements java.io.Serializable {
    private com.lytx.dto.NoteInfo[] eventNotes;

    private java.lang.Boolean isCoachable;

    private com.lytx.dto.NoteInfo[] reviewNotes;

    private com.lytx.dto.NoteInfo[] sessionNotes;

    public EventsInfoV4() {
    }

    public EventsInfoV4(
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
           com.lytx.dto.NoteInfo[] sessionNotes) {
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
            eventLink);
        this.eventNotes = eventNotes;
        this.isCoachable = isCoachable;
        this.reviewNotes = reviewNotes;
        this.sessionNotes = sessionNotes;
    }


    /**
     * Gets the eventNotes value for this EventsInfoV4.
     * 
     * @return eventNotes
     */
    public com.lytx.dto.NoteInfo[] getEventNotes() {
        return eventNotes;
    }


    /**
     * Sets the eventNotes value for this EventsInfoV4.
     * 
     * @param eventNotes
     */
    public void setEventNotes(com.lytx.dto.NoteInfo[] eventNotes) {
        this.eventNotes = eventNotes;
    }


    /**
     * Gets the isCoachable value for this EventsInfoV4.
     * 
     * @return isCoachable
     */
    public java.lang.Boolean getIsCoachable() {
        return isCoachable;
    }


    /**
     * Sets the isCoachable value for this EventsInfoV4.
     * 
     * @param isCoachable
     */
    public void setIsCoachable(java.lang.Boolean isCoachable) {
        this.isCoachable = isCoachable;
    }


    /**
     * Gets the reviewNotes value for this EventsInfoV4.
     * 
     * @return reviewNotes
     */
    public com.lytx.dto.NoteInfo[] getReviewNotes() {
        return reviewNotes;
    }


    /**
     * Sets the reviewNotes value for this EventsInfoV4.
     * 
     * @param reviewNotes
     */
    public void setReviewNotes(com.lytx.dto.NoteInfo[] reviewNotes) {
        this.reviewNotes = reviewNotes;
    }


    /**
     * Gets the sessionNotes value for this EventsInfoV4.
     * 
     * @return sessionNotes
     */
    public com.lytx.dto.NoteInfo[] getSessionNotes() {
        return sessionNotes;
    }


    /**
     * Sets the sessionNotes value for this EventsInfoV4.
     * 
     * @param sessionNotes
     */
    public void setSessionNotes(com.lytx.dto.NoteInfo[] sessionNotes) {
        this.sessionNotes = sessionNotes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EventsInfoV4)) return false;
        EventsInfoV4 other = (EventsInfoV4) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.eventNotes==null && other.getEventNotes()==null) || 
             (this.eventNotes!=null &&
              java.util.Arrays.equals(this.eventNotes, other.getEventNotes()))) &&
            ((this.isCoachable==null && other.getIsCoachable()==null) || 
             (this.isCoachable!=null &&
              this.isCoachable.equals(other.getIsCoachable()))) &&
            ((this.reviewNotes==null && other.getReviewNotes()==null) || 
             (this.reviewNotes!=null &&
              java.util.Arrays.equals(this.reviewNotes, other.getReviewNotes()))) &&
            ((this.sessionNotes==null && other.getSessionNotes()==null) || 
             (this.sessionNotes!=null &&
              java.util.Arrays.equals(this.sessionNotes, other.getSessionNotes())));
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
        if (getEventNotes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEventNotes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEventNotes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIsCoachable() != null) {
            _hashCode += getIsCoachable().hashCode();
        }
        if (getReviewNotes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReviewNotes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReviewNotes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSessionNotes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSessionNotes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSessionNotes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EventsInfoV4.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfoV4"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventNotes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventNotes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isCoachable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "IsCoachable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reviewNotes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ReviewNotes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionNotes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "SessionNotes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
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
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
