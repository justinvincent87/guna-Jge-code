/**
 * GetVehicleStatusesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class GetVehicleStatusesResponse  implements java.io.Serializable {
    private java.lang.Boolean error;

    private java.lang.String message;

    private com.lytx.dto.VehicleStatus[] vehicleStatuses;

    public GetVehicleStatusesResponse() {
    }

    public GetVehicleStatusesResponse(
           java.lang.Boolean error,
           java.lang.String message,
           com.lytx.dto.VehicleStatus[] vehicleStatuses) {
           this.error = error;
           this.message = message;
           this.vehicleStatuses = vehicleStatuses;
    }


    /**
     * Gets the error value for this GetVehicleStatusesResponse.
     * 
     * @return error
     */
    public java.lang.Boolean getError() {
        return error;
    }


    /**
     * Sets the error value for this GetVehicleStatusesResponse.
     * 
     * @param error
     */
    public void setError(java.lang.Boolean error) {
        this.error = error;
    }


    /**
     * Gets the message value for this GetVehicleStatusesResponse.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this GetVehicleStatusesResponse.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the vehicleStatuses value for this GetVehicleStatusesResponse.
     * 
     * @return vehicleStatuses
     */
    public com.lytx.dto.VehicleStatus[] getVehicleStatuses() {
        return vehicleStatuses;
    }


    /**
     * Sets the vehicleStatuses value for this GetVehicleStatusesResponse.
     * 
     * @param vehicleStatuses
     */
    public void setVehicleStatuses(com.lytx.dto.VehicleStatus[] vehicleStatuses) {
        this.vehicleStatuses = vehicleStatuses;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetVehicleStatusesResponse)) return false;
        GetVehicleStatusesResponse other = (GetVehicleStatusesResponse) obj;
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
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.vehicleStatuses==null && other.getVehicleStatuses()==null) || 
             (this.vehicleStatuses!=null &&
              java.util.Arrays.equals(this.vehicleStatuses, other.getVehicleStatuses())));
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
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getVehicleStatuses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVehicleStatuses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVehicleStatuses(), i);
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
        new org.apache.axis.description.TypeDesc(GetVehicleStatusesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GetVehicleStatusesResponse"));
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
        elemField.setFieldName("vehicleStatuses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleStatuses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleStatus"));
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
