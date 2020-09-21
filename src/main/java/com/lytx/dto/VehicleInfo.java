/**
 * VehicleInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class VehicleInfo  implements java.io.Serializable {
    private java.lang.Long assignedDriver;

    private java.util.Calendar creationDate;

    private java.util.Calendar inServiceDate;

    private java.util.Calendar lastConnected;

    private java.lang.String licenseNumber;

    private java.lang.String name;

    private java.lang.Integer objectRevision;

    private java.util.Calendar revisionDate;

    private java.lang.Long vehicleId;

    private java.lang.Integer vehicleStatusId;

    private java.lang.Long vehicleTypeId;

    public VehicleInfo() {
    }

    public VehicleInfo(
           java.lang.Long assignedDriver,
           java.util.Calendar creationDate,
           java.util.Calendar inServiceDate,
           java.util.Calendar lastConnected,
           java.lang.String licenseNumber,
           java.lang.String name,
           java.lang.Integer objectRevision,
           java.util.Calendar revisionDate,
           java.lang.Long vehicleId,
           java.lang.Integer vehicleStatusId,
           java.lang.Long vehicleTypeId) {
           this.assignedDriver = assignedDriver;
           this.creationDate = creationDate;
           this.inServiceDate = inServiceDate;
           this.lastConnected = lastConnected;
           this.licenseNumber = licenseNumber;
           this.name = name;
           this.objectRevision = objectRevision;
           this.revisionDate = revisionDate;
           this.vehicleId = vehicleId;
           this.vehicleStatusId = vehicleStatusId;
           this.vehicleTypeId = vehicleTypeId;
    }


    /**
     * Gets the assignedDriver value for this VehicleInfo.
     * 
     * @return assignedDriver
     */
    public java.lang.Long getAssignedDriver() {
        return assignedDriver;
    }


    /**
     * Sets the assignedDriver value for this VehicleInfo.
     * 
     * @param assignedDriver
     */
    public void setAssignedDriver(java.lang.Long assignedDriver) {
        this.assignedDriver = assignedDriver;
    }


    /**
     * Gets the creationDate value for this VehicleInfo.
     * 
     * @return creationDate
     */
    public java.util.Calendar getCreationDate() {
        return creationDate;
    }


    /**
     * Sets the creationDate value for this VehicleInfo.
     * 
     * @param creationDate
     */
    public void setCreationDate(java.util.Calendar creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Gets the inServiceDate value for this VehicleInfo.
     * 
     * @return inServiceDate
     */
    public java.util.Calendar getInServiceDate() {
        return inServiceDate;
    }


    /**
     * Sets the inServiceDate value for this VehicleInfo.
     * 
     * @param inServiceDate
     */
    public void setInServiceDate(java.util.Calendar inServiceDate) {
        this.inServiceDate = inServiceDate;
    }


    /**
     * Gets the lastConnected value for this VehicleInfo.
     * 
     * @return lastConnected
     */
    public java.util.Calendar getLastConnected() {
        return lastConnected;
    }


    /**
     * Sets the lastConnected value for this VehicleInfo.
     * 
     * @param lastConnected
     */
    public void setLastConnected(java.util.Calendar lastConnected) {
        this.lastConnected = lastConnected;
    }


    /**
     * Gets the licenseNumber value for this VehicleInfo.
     * 
     * @return licenseNumber
     */
    public java.lang.String getLicenseNumber() {
        return licenseNumber;
    }


    /**
     * Sets the licenseNumber value for this VehicleInfo.
     * 
     * @param licenseNumber
     */
    public void setLicenseNumber(java.lang.String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


    /**
     * Gets the name value for this VehicleInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this VehicleInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the objectRevision value for this VehicleInfo.
     * 
     * @return objectRevision
     */
    public java.lang.Integer getObjectRevision() {
        return objectRevision;
    }


    /**
     * Sets the objectRevision value for this VehicleInfo.
     * 
     * @param objectRevision
     */
    public void setObjectRevision(java.lang.Integer objectRevision) {
        this.objectRevision = objectRevision;
    }


    /**
     * Gets the revisionDate value for this VehicleInfo.
     * 
     * @return revisionDate
     */
    public java.util.Calendar getRevisionDate() {
        return revisionDate;
    }


    /**
     * Sets the revisionDate value for this VehicleInfo.
     * 
     * @param revisionDate
     */
    public void setRevisionDate(java.util.Calendar revisionDate) {
        this.revisionDate = revisionDate;
    }


    /**
     * Gets the vehicleId value for this VehicleInfo.
     * 
     * @return vehicleId
     */
    public java.lang.Long getVehicleId() {
        return vehicleId;
    }


    /**
     * Sets the vehicleId value for this VehicleInfo.
     * 
     * @param vehicleId
     */
    public void setVehicleId(java.lang.Long vehicleId) {
        this.vehicleId = vehicleId;
    }


    /**
     * Gets the vehicleStatusId value for this VehicleInfo.
     * 
     * @return vehicleStatusId
     */
    public java.lang.Integer getVehicleStatusId() {
        return vehicleStatusId;
    }


    /**
     * Sets the vehicleStatusId value for this VehicleInfo.
     * 
     * @param vehicleStatusId
     */
    public void setVehicleStatusId(java.lang.Integer vehicleStatusId) {
        this.vehicleStatusId = vehicleStatusId;
    }


    /**
     * Gets the vehicleTypeId value for this VehicleInfo.
     * 
     * @return vehicleTypeId
     */
    public java.lang.Long getVehicleTypeId() {
        return vehicleTypeId;
    }


    /**
     * Sets the vehicleTypeId value for this VehicleInfo.
     * 
     * @param vehicleTypeId
     */
    public void setVehicleTypeId(java.lang.Long vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VehicleInfo)) return false;
        VehicleInfo other = (VehicleInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.assignedDriver==null && other.getAssignedDriver()==null) || 
             (this.assignedDriver!=null &&
              this.assignedDriver.equals(other.getAssignedDriver()))) &&
            ((this.creationDate==null && other.getCreationDate()==null) || 
             (this.creationDate!=null &&
              this.creationDate.equals(other.getCreationDate()))) &&
            ((this.inServiceDate==null && other.getInServiceDate()==null) || 
             (this.inServiceDate!=null &&
              this.inServiceDate.equals(other.getInServiceDate()))) &&
            ((this.lastConnected==null && other.getLastConnected()==null) || 
             (this.lastConnected!=null &&
              this.lastConnected.equals(other.getLastConnected()))) &&
            ((this.licenseNumber==null && other.getLicenseNumber()==null) || 
             (this.licenseNumber!=null &&
              this.licenseNumber.equals(other.getLicenseNumber()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.objectRevision==null && other.getObjectRevision()==null) || 
             (this.objectRevision!=null &&
              this.objectRevision.equals(other.getObjectRevision()))) &&
            ((this.revisionDate==null && other.getRevisionDate()==null) || 
             (this.revisionDate!=null &&
              this.revisionDate.equals(other.getRevisionDate()))) &&
            ((this.vehicleId==null && other.getVehicleId()==null) || 
             (this.vehicleId!=null &&
              this.vehicleId.equals(other.getVehicleId()))) &&
            ((this.vehicleStatusId==null && other.getVehicleStatusId()==null) || 
             (this.vehicleStatusId!=null &&
              this.vehicleStatusId.equals(other.getVehicleStatusId()))) &&
            ((this.vehicleTypeId==null && other.getVehicleTypeId()==null) || 
             (this.vehicleTypeId!=null &&
              this.vehicleTypeId.equals(other.getVehicleTypeId())));
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
        if (getAssignedDriver() != null) {
            _hashCode += getAssignedDriver().hashCode();
        }
        if (getCreationDate() != null) {
            _hashCode += getCreationDate().hashCode();
        }
        if (getInServiceDate() != null) {
            _hashCode += getInServiceDate().hashCode();
        }
        if (getLastConnected() != null) {
            _hashCode += getLastConnected().hashCode();
        }
        if (getLicenseNumber() != null) {
            _hashCode += getLicenseNumber().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getObjectRevision() != null) {
            _hashCode += getObjectRevision().hashCode();
        }
        if (getRevisionDate() != null) {
            _hashCode += getRevisionDate().hashCode();
        }
        if (getVehicleId() != null) {
            _hashCode += getVehicleId().hashCode();
        }
        if (getVehicleStatusId() != null) {
            _hashCode += getVehicleStatusId().hashCode();
        }
        if (getVehicleTypeId() != null) {
            _hashCode += getVehicleTypeId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VehicleInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedDriver");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "AssignedDriver"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "CreationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inServiceDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "InServiceDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastConnected");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "LastConnected"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("licenseNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "LicenseNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objectRevision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "ObjectRevision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("revisionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "RevisionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vehicleId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vehicleStatusId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "VehicleStatusId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
