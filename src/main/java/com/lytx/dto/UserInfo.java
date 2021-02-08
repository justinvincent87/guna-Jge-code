/**
 * UserInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.util.Calendar	creationDate;
	private java.util.Calendar	dob;
	private java.lang.String	email;
	private java.lang.String	employeeNumber;
	private java.util.Calendar	employmentEnd;
	private java.util.Calendar	employmentStart;
	private java.lang.String	firstName;
	private java.lang.String	gender;
	private java.lang.String	lastName;
	private java.lang.Integer	objectRevision;
	private java.util.Calendar	revisionDate;
	private java.lang.Long		userId;
	private java.lang.String	userName;

	public UserInfo() {
	}

	public UserInfo(java.util.Calendar creationDate, java.util.Calendar dob, java.lang.String email,
	        java.lang.String employeeNumber, java.util.Calendar employmentEnd, java.util.Calendar employmentStart,
	        java.lang.String firstName, java.lang.String gender, java.lang.String lastName,
	        java.lang.Integer objectRevision, java.util.Calendar revisionDate, java.lang.Long userId,
	        java.lang.String userName) {
		this.creationDate		= creationDate;
		this.dob				= dob;
		this.email				= email;
		this.employeeNumber		= employeeNumber;
		this.employmentEnd		= employmentEnd;
		this.employmentStart	= employmentStart;
		this.firstName			= firstName;
		this.gender				= gender;
		this.lastName			= lastName;
		this.objectRevision		= objectRevision;
		this.revisionDate		= revisionDate;
		this.userId				= userId;
		this.userName			= userName;
	}

	/**
	 * Gets the creationDate value for this UserInfo.
	 * 
	 * @return creationDate
	 */
	public java.util.Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creationDate value for this UserInfo.
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(java.util.Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the dob value for this UserInfo.
	 * 
	 * @return dob
	 */
	public java.util.Calendar getDob() {
		return dob;
	}

	/**
	 * Sets the dob value for this UserInfo.
	 * 
	 * @param dob
	 */
	public void setDob(java.util.Calendar dob) {
		this.dob = dob;
	}

	/**
	 * Gets the email value for this UserInfo.
	 * 
	 * @return email
	 */
	public java.lang.String getEmail() {
		return email;
	}

	/**
	 * Sets the email value for this UserInfo.
	 * 
	 * @param email
	 */
	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	/**
	 * Gets the employeeNumber value for this UserInfo.
	 * 
	 * @return employeeNumber
	 */
	public java.lang.String getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * Sets the employeeNumber value for this UserInfo.
	 * 
	 * @param employeeNumber
	 */
	public void setEmployeeNumber(java.lang.String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/**
	 * Gets the employmentEnd value for this UserInfo.
	 * 
	 * @return employmentEnd
	 */
	public java.util.Calendar getEmploymentEnd() {
		return employmentEnd;
	}

	/**
	 * Sets the employmentEnd value for this UserInfo.
	 * 
	 * @param employmentEnd
	 */
	public void setEmploymentEnd(java.util.Calendar employmentEnd) {
		this.employmentEnd = employmentEnd;
	}

	/**
	 * Gets the employmentStart value for this UserInfo.
	 * 
	 * @return employmentStart
	 */
	public java.util.Calendar getEmploymentStart() {
		return employmentStart;
	}

	/**
	 * Sets the employmentStart value for this UserInfo.
	 * 
	 * @param employmentStart
	 */
	public void setEmploymentStart(java.util.Calendar employmentStart) {
		this.employmentStart = employmentStart;
	}

	/**
	 * Gets the firstName value for this UserInfo.
	 * 
	 * @return firstName
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName value for this UserInfo.
	 * 
	 * @param firstName
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the gender value for this UserInfo.
	 * 
	 * @return gender
	 */
	public java.lang.String getGender() {
		return gender;
	}

	/**
	 * Sets the gender value for this UserInfo.
	 * 
	 * @param gender
	 */
	public void setGender(java.lang.String gender) {
		this.gender = gender;
	}

	/**
	 * Gets the lastName value for this UserInfo.
	 * 
	 * @return lastName
	 */
	public java.lang.String getLastName() {
		return lastName;
	}

	/**
	 * Sets the lastName value for this UserInfo.
	 * 
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the objectRevision value for this UserInfo.
	 * 
	 * @return objectRevision
	 */
	public java.lang.Integer getObjectRevision() {
		return objectRevision;
	}

	/**
	 * Sets the objectRevision value for this UserInfo.
	 * 
	 * @param objectRevision
	 */
	public void setObjectRevision(java.lang.Integer objectRevision) {
		this.objectRevision = objectRevision;
	}

	/**
	 * Gets the revisionDate value for this UserInfo.
	 * 
	 * @return revisionDate
	 */
	public java.util.Calendar getRevisionDate() {
		return revisionDate;
	}

	/**
	 * Sets the revisionDate value for this UserInfo.
	 * 
	 * @param revisionDate
	 */
	public void setRevisionDate(java.util.Calendar revisionDate) {
		this.revisionDate = revisionDate;
	}

	/**
	 * Gets the userId value for this UserInfo.
	 * 
	 * @return userId
	 */
	public java.lang.Long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this UserInfo.
	 * 
	 * @param userId
	 */
	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this UserInfo.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this UserInfo.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof UserInfo))
			return false;
		UserInfo other = (UserInfo) obj;
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
		        && ((this.dob == null && other.getDob() == null)
		                || (this.dob != null && this.dob.equals(other.getDob())))
		        && ((this.email == null && other.getEmail() == null)
		                || (this.email != null && this.email.equals(other.getEmail())))
		        && ((this.employeeNumber == null && other.getEmployeeNumber() == null)
		                || (this.employeeNumber != null && this.employeeNumber.equals(other.getEmployeeNumber())))
		        && ((this.employmentEnd == null && other.getEmploymentEnd() == null)
		                || (this.employmentEnd != null && this.employmentEnd.equals(other.getEmploymentEnd())))
		        && ((this.employmentStart == null && other.getEmploymentStart() == null)
		                || (this.employmentStart != null && this.employmentStart.equals(other.getEmploymentStart())))
		        && ((this.firstName == null && other.getFirstName() == null)
		                || (this.firstName != null && this.firstName.equals(other.getFirstName())))
		        && ((this.gender == null && other.getGender() == null)
		                || (this.gender != null && this.gender.equals(other.getGender())))
		        && ((this.lastName == null && other.getLastName() == null)
		                || (this.lastName != null && this.lastName.equals(other.getLastName())))
		        && ((this.objectRevision == null && other.getObjectRevision() == null)
		                || (this.objectRevision != null && this.objectRevision.equals(other.getObjectRevision())))
		        && ((this.revisionDate == null && other.getRevisionDate() == null)
		                || (this.revisionDate != null && this.revisionDate.equals(other.getRevisionDate())))
		        && ((this.userId == null && other.getUserId() == null)
		                || (this.userId != null && this.userId.equals(other.getUserId())))
		        && ((this.userName == null && other.getUserName() == null)
		                || (this.userName != null && this.userName.equals(other.getUserName())));
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
		if (getDob() != null) {
			_hashCode += getDob().hashCode();
		}
		if (getEmail() != null) {
			_hashCode += getEmail().hashCode();
		}
		if (getEmployeeNumber() != null) {
			_hashCode += getEmployeeNumber().hashCode();
		}
		if (getEmploymentEnd() != null) {
			_hashCode += getEmploymentEnd().hashCode();
		}
		if (getEmploymentStart() != null) {
			_hashCode += getEmploymentStart().hashCode();
		}
		if (getFirstName() != null) {
			_hashCode += getFirstName().hashCode();
		}
		if (getGender() != null) {
			_hashCode += getGender().hashCode();
		}
		if (getLastName() != null) {
			_hashCode += getLastName().hashCode();
		}
		if (getObjectRevision() != null) {
			_hashCode += getObjectRevision().hashCode();
		}
		if (getRevisionDate() != null) {
			_hashCode += getRevisionDate().hashCode();
		}
		if (getUserId() != null) {
			_hashCode += getUserId().hashCode();
		}
		if (getUserName() != null) {
			_hashCode += getUserName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        UserInfo.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "UserInfo"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("creationDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "CreationDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dob");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Dob"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("email");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Email"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("employeeNumber");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EmployeeNumber"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("employmentEnd");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EmploymentEnd"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("employmentStart");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "EmploymentStart"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("firstName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "FirstName"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("gender");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "Gender"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("lastName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "LastName"));
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
		elemField.setFieldName("userId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "UserId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("userName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes", "UserName"));
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
