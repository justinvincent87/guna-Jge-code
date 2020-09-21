/**
 * GroupInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class GroupInfo  implements java.io.Serializable {
    private java.util.Calendar creationDate;

    private java.lang.String fullPath;

    private java.lang.Long groupId;

    private java.lang.String name;

    private java.lang.Integer objectRevision;

    private java.util.Calendar revisionDate;

    public GroupInfo() {
    }

    public GroupInfo(
           java.util.Calendar creationDate,
           java.lang.String fullPath,
           java.lang.Long groupId,
           java.lang.String name,
           java.lang.Integer objectRevision,
           java.util.Calendar revisionDate) {
           this.creationDate = creationDate;
           this.fullPath = fullPath;
           this.groupId = groupId;
           this.name = name;
           this.objectRevision = objectRevision;
           this.revisionDate = revisionDate;
    }


    /**
     * Gets the creationDate value for this GroupInfo.
     * 
     * @return creationDate
     */
    public java.util.Calendar getCreationDate() {
        return creationDate;
    }


    /**
     * Sets the creationDate value for this GroupInfo.
     * 
     * @param creationDate
     */
    public void setCreationDate(java.util.Calendar creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Gets the fullPath value for this GroupInfo.
     * 
     * @return fullPath
     */
    public java.lang.String getFullPath() {
        return fullPath;
    }


    /**
     * Sets the fullPath value for this GroupInfo.
     * 
     * @param fullPath
     */
    public void setFullPath(java.lang.String fullPath) {
        this.fullPath = fullPath;
    }


    /**
     * Gets the groupId value for this GroupInfo.
     * 
     * @return groupId
     */
    public java.lang.Long getGroupId() {
        return groupId;
    }


    /**
     * Sets the groupId value for this GroupInfo.
     * 
     * @param groupId
     */
    public void setGroupId(java.lang.Long groupId) {
        this.groupId = groupId;
    }


    /**
     * Gets the name value for this GroupInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this GroupInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the objectRevision value for this GroupInfo.
     * 
     * @return objectRevision
     */
    public java.lang.Integer getObjectRevision() {
        return objectRevision;
    }


    /**
     * Sets the objectRevision value for this GroupInfo.
     * 
     * @param objectRevision
     */
    public void setObjectRevision(java.lang.Integer objectRevision) {
        this.objectRevision = objectRevision;
    }


    /**
     * Gets the revisionDate value for this GroupInfo.
     * 
     * @return revisionDate
     */
    public java.util.Calendar getRevisionDate() {
        return revisionDate;
    }


    /**
     * Sets the revisionDate value for this GroupInfo.
     * 
     * @param revisionDate
     */
    public void setRevisionDate(java.util.Calendar revisionDate) {
        this.revisionDate = revisionDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GroupInfo)) return false;
        GroupInfo other = (GroupInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.creationDate==null && other.getCreationDate()==null) || 
             (this.creationDate!=null &&
              this.creationDate.equals(other.getCreationDate()))) &&
            ((this.fullPath==null && other.getFullPath()==null) || 
             (this.fullPath!=null &&
              this.fullPath.equals(other.getFullPath()))) &&
            ((this.groupId==null && other.getGroupId()==null) || 
             (this.groupId!=null &&
              this.groupId.equals(other.getGroupId()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.objectRevision==null && other.getObjectRevision()==null) || 
             (this.objectRevision!=null &&
              this.objectRevision.equals(other.getObjectRevision()))) &&
            ((this.revisionDate==null && other.getRevisionDate()==null) || 
             (this.revisionDate!=null &&
              this.revisionDate.equals(other.getRevisionDate())));
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
        if (getCreationDate() != null) {
            _hashCode += getCreationDate().hashCode();
        }
        if (getFullPath() != null) {
            _hashCode += getFullPath().hashCode();
        }
        if (getGroupId() != null) {
            _hashCode += getGroupId().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GroupInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GroupInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "CreationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullPath");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "FullPath"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "GroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
