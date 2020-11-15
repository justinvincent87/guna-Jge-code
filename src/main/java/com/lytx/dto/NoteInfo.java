/**
 * NoteInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class NoteInfo  implements java.io.Serializable {
    private java.lang.String authorFirstName;

    private java.lang.String authorLastName;

    private java.lang.String authorUserName;

    private java.lang.String content;

    private java.util.Calendar time;

    public NoteInfo() {
    }

    public NoteInfo(
           java.lang.String authorFirstName,
           java.lang.String authorLastName,
           java.lang.String authorUserName,
           java.lang.String content,
           java.util.Calendar time) {
           this.authorFirstName = authorFirstName;
           this.authorLastName = authorLastName;
           this.authorUserName = authorUserName;
           this.content = content;
           this.time = time;
    }


    /**
     * Gets the authorFirstName value for this NoteInfo.
     * 
     * @return authorFirstName
     */
    public java.lang.String getAuthorFirstName() {
        return authorFirstName;
    }


    /**
     * Sets the authorFirstName value for this NoteInfo.
     * 
     * @param authorFirstName
     */
    public void setAuthorFirstName(java.lang.String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }


    /**
     * Gets the authorLastName value for this NoteInfo.
     * 
     * @return authorLastName
     */
    public java.lang.String getAuthorLastName() {
        return authorLastName;
    }


    /**
     * Sets the authorLastName value for this NoteInfo.
     * 
     * @param authorLastName
     */
    public void setAuthorLastName(java.lang.String authorLastName) {
        this.authorLastName = authorLastName;
    }


    /**
     * Gets the authorUserName value for this NoteInfo.
     * 
     * @return authorUserName
     */
    public java.lang.String getAuthorUserName() {
        return authorUserName;
    }


    /**
     * Sets the authorUserName value for this NoteInfo.
     * 
     * @param authorUserName
     */
    public void setAuthorUserName(java.lang.String authorUserName) {
        this.authorUserName = authorUserName;
    }


    /**
     * Gets the content value for this NoteInfo.
     * 
     * @return content
     */
    public java.lang.String getContent() {
        return content;
    }


    /**
     * Sets the content value for this NoteInfo.
     * 
     * @param content
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }


    /**
     * Gets the time value for this NoteInfo.
     * 
     * @return time
     */
    public java.util.Calendar getTime() {
        return time;
    }


    /**
     * Sets the time value for this NoteInfo.
     * 
     * @param time
     */
    public void setTime(java.util.Calendar time) {
        this.time = time;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NoteInfo)) return false;
        NoteInfo other = (NoteInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.authorFirstName==null && other.getAuthorFirstName()==null) || 
             (this.authorFirstName!=null &&
              this.authorFirstName.equals(other.getAuthorFirstName()))) &&
            ((this.authorLastName==null && other.getAuthorLastName()==null) || 
             (this.authorLastName!=null &&
              this.authorLastName.equals(other.getAuthorLastName()))) &&
            ((this.authorUserName==null && other.getAuthorUserName()==null) || 
             (this.authorUserName!=null &&
              this.authorUserName.equals(other.getAuthorUserName()))) &&
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              this.content.equals(other.getContent()))) &&
            ((this.time==null && other.getTime()==null) || 
             (this.time!=null &&
              this.time.equals(other.getTime())));
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
        if (getAuthorFirstName() != null) {
            _hashCode += getAuthorFirstName().hashCode();
        }
        if (getAuthorLastName() != null) {
            _hashCode += getAuthorLastName().hashCode();
        }
        if (getAuthorUserName() != null) {
            _hashCode += getAuthorUserName().hashCode();
        }
        if (getContent() != null) {
            _hashCode += getContent().hashCode();
        }
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NoteInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "NoteInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorFirstName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "AuthorFirstName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorLastName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "AuthorLastName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorUserName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "AuthorUserName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Time"));
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
