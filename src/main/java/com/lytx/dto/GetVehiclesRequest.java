/**
 * GetVehiclesRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class GetVehiclesRequest  implements java.io.Serializable {
    private java.lang.Long groupId;

    private java.lang.Boolean includeSubgroups;

    private java.lang.String sessionId;

    public GetVehiclesRequest() {
    }

    public GetVehiclesRequest(
           java.lang.Long groupId,
           java.lang.Boolean includeSubgroups,
           java.lang.String sessionId) {
           this.groupId = groupId;
           this.includeSubgroups = includeSubgroups;
           this.sessionId = sessionId;
    }


    /**
     * Gets the groupId value for this GetVehiclesRequest.
     * 
     * @return groupId
     */
    public java.lang.Long getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GetVehiclesRequest.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the includeSubgroups value for this GetVehiclesRequest.
     * 
     * @return includeSubgroups
     */
    public java.lang.Boolean getIncludeSubgroups() {
        return includeSubgroups;
    }


    /**
     * Sets the includeSubgroups value for this GetVehiclesRequest.
     * 
     * @param includeSubgroups
     */
    public void setIncludeSubgroups(java.lang.Boolean includeSubgroups) {
        this.includeSubgroups = includeSubgroups;
    }


    /**
     * Gets the sessionId value for this GetVehiclesRequest.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetVehiclesRequest.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetVehiclesRequest)) return false;
        GetVehiclesRequest other = (GetVehiclesRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.groupId==null && other.getGroupId()==null) || 
             (this.groupId!=null &&
              this.groupId.equals(other.getGroupId()))) &&
            ((this.includeSubgroups==null && other.getIncludeSubgroups()==null) || 
             (this.includeSubgroups!=null &&
              this.includeSubgroups.equals(other.getIncludeSubgroups()))) &&
            ((this.sessionId==null && other.getSessionId()==null) || 
             (this.sessionId!=null &&
              this.sessionId.equals(other.getSessionId())));
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
        if (getGroupId() != null) {
            _hashCode += getGroupId().hashCode();
        }
        if (getIncludeSubgroups() != null) {
            _hashCode += getIncludeSubgroups().hashCode();
        }
        if (getSessionId() != null) {
            _hashCode += getSessionId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetVehiclesRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GetVehiclesRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeSubgroups");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "IncludeSubgroups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
