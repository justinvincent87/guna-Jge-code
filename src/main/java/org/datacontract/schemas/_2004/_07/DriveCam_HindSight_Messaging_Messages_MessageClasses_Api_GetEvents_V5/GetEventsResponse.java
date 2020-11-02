/**
 * GetEventsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api_GetEvents_V5;

public class GetEventsResponse  implements java.io.Serializable {
    private java.lang.Boolean error;

    private com.lytx.dto.EventsInfoV5[] events;

    private java.lang.String message;

    private java.util.Calendar queryCutoff;

    public GetEventsResponse() {
    }

    public GetEventsResponse(
           java.lang.Boolean error,
           com.lytx.dto.EventsInfoV5[] events,
           java.lang.String message,
           java.util.Calendar queryCutoff) {
           this.error = error;
           this.events = events;
           this.message = message;
           this.queryCutoff = queryCutoff;
    }


    /**
     * Gets the error value for this GetEventsResponse.
     * 
     * @return error
     */
    public java.lang.Boolean getError() {
        return error;
    }


    /**
     * Sets the error value for this GetEventsResponse.
     * 
     * @param error
     */
    public void setError(java.lang.Boolean error) {
        this.error = error;
    }


    /**
     * Gets the events value for this GetEventsResponse.
     * 
     * @return events
     */
    public com.lytx.dto.EventsInfoV5[] getEvents() {
        return events;
    }


    /**
     * Sets the events value for this GetEventsResponse.
     * 
     * @param events
     */
    public void setEvents(com.lytx.dto.EventsInfoV5[] events) {
        this.events = events;
    }


    /**
     * Gets the message value for this GetEventsResponse.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this GetEventsResponse.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the queryCutoff value for this GetEventsResponse.
     * 
     * @return queryCutoff
     */
    public java.util.Calendar getQueryCutoff() {
        return queryCutoff;
    }


    /**
     * Sets the queryCutoff value for this GetEventsResponse.
     * 
     * @param queryCutoff
     */
    public void setQueryCutoff(java.util.Calendar queryCutoff) {
        this.queryCutoff = queryCutoff;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEventsResponse)) return false;
        GetEventsResponse other = (GetEventsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.error==null && other.getError()==null) || 
             (this.error!=null &&
              this.error.equals(other.getError()))) &&
            ((this.events==null && other.getEvents()==null) || 
             (this.events!=null &&
              java.util.Arrays.equals(this.events, other.getEvents()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.queryCutoff==null && other.getQueryCutoff()==null) || 
             (this.queryCutoff!=null &&
              this.queryCutoff.equals(other.getQueryCutoff())));
        __equalsCalc = null;
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
        if (getEvents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEvents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEvents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getQueryCutoff() != null) {
            _hashCode += getQueryCutoff().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetEventsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api.GetEvents_V5", "GetEventsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api.GetEvents_V5", "Error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("events");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api.GetEvents_V5", "Events"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfoV5"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfoV5"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api.GetEvents_V5", "Message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryCutoff");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api.GetEvents_V5", "QueryCutoff"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
