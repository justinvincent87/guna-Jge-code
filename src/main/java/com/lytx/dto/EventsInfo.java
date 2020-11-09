/**
 * EventsInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.lytx.dto;

public class EventsInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	private org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior[] behaviors;

	private java.lang.Long			coachId;
	private java.util.Calendar		creationDate;
	private java.lang.Long			customerEventId;
	private java.lang.String		customerEventIdString;
	private java.util.Calendar		downloadedDate;
	private java.lang.Long			driverId;
	private java.lang.String		ERSerialNumber;
	private java.lang.Long			eventId;
	private java.lang.Long			eventStatusId;
	private java.lang.Long			eventTypeId;
	private java.math.BigDecimal	forwardMax;
	private java.math.BigDecimal	forwardThreshold;
	private java.lang.Long			groupId;
	private java.math.BigDecimal	heading;
	private java.util.Calendar		lastUpdated;
	private java.math.BigDecimal	lateralMax;
	private java.math.BigDecimal	lateralThreshold;
	private java.math.BigDecimal	latitude;
	private java.math.BigDecimal	longitude;
	private java.lang.String		notes;
	private java.lang.Integer		objectRevision;
	private java.math.BigDecimal	overdue;
	private java.lang.String		recordDateTZ;
	private java.util.Calendar		recordDateUTC;
	private java.lang.Integer		recordDateUtcOffset;
	private java.util.Calendar		reviewedDate;
	private java.util.Calendar		revisionDate;
	private java.lang.Long			score;
	private java.math.BigDecimal	shockThreshold;
	private java.math.BigDecimal	speed;
	private java.lang.Long			vehicleId;

	public EventsInfo() {
	}

	public EventsInfo(
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
	        java.lang.Long vehicleId) {
		this.behaviors				= behaviors;
		this.coachId				= coachId;
		this.creationDate			= creationDate;
		this.customerEventId		= customerEventId;
		this.customerEventIdString	= customerEventIdString;
		this.downloadedDate			= downloadedDate;
		this.driverId				= driverId;
		this.ERSerialNumber			= ERSerialNumber;
		this.eventId				= eventId;
		this.eventStatusId			= eventStatusId;
		this.eventTypeId			= eventTypeId;
		this.forwardMax				= forwardMax;
		this.forwardThreshold		= forwardThreshold;
		this.groupId				= groupId;
		this.heading				= heading;
		this.lastUpdated			= lastUpdated;
		this.lateralMax				= lateralMax;
		this.lateralThreshold		= lateralThreshold;
		this.latitude				= latitude;
		this.longitude				= longitude;
		this.notes					= notes;
		this.objectRevision			= objectRevision;
		this.overdue				= overdue;
		this.recordDateTZ			= recordDateTZ;
		this.recordDateUTC			= recordDateUTC;
		this.recordDateUtcOffset	= recordDateUtcOffset;
		this.reviewedDate			= reviewedDate;
		this.revisionDate			= revisionDate;
		this.score					= score;
		this.shockThreshold			= shockThreshold;
		this.speed					= speed;
		this.vehicleId				= vehicleId;
	}

	/**
	 * Gets the behaviors value for this EventsInfo.
	 * 
	 * @return behaviors
	 */
	public org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior[] getBehaviors() {
		return behaviors;
	}

	/**
	 * Sets the behaviors value for this EventsInfo.
	 * 
	 * @param behaviors
	 */
	public void setBehaviors(
	        org.datacontract.schemas._2004._07.DriveCam_HindSight_Messaging_Messages_MessageClasses_Api.EventBehavior[] behaviors) {
		this.behaviors = behaviors;
	}

	/**
	 * Gets the coachId value for this EventsInfo.
	 * 
	 * @return coachId
	 */
	public java.lang.Long getCoachId() {
		return coachId;
	}

	/**
	 * Sets the coachId value for this EventsInfo.
	 * 
	 * @param coachId
	 */
	public void setCoachId(java.lang.Long coachId) {
		this.coachId = coachId;
	}

	/**
	 * Gets the creationDate value for this EventsInfo.
	 * 
	 * @return creationDate
	 */
	public java.util.Calendar getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creationDate value for this EventsInfo.
	 * 
	 * @param creationDate
	 */
	public void setCreationDate(java.util.Calendar creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the customerEventId value for this EventsInfo.
	 * 
	 * @return customerEventId
	 */
	public java.lang.Long getCustomerEventId() {
		return customerEventId;
	}

	/**
	 * Sets the customerEventId value for this EventsInfo.
	 * 
	 * @param customerEventId
	 */
	public void setCustomerEventId(java.lang.Long customerEventId) {
		this.customerEventId = customerEventId;
	}

	/**
	 * Gets the customerEventIdString value for this EventsInfo.
	 * 
	 * @return customerEventIdString
	 */
	public java.lang.String getCustomerEventIdString() {
		return customerEventIdString;
	}

	/**
	 * Sets the customerEventIdString value for this EventsInfo.
	 * 
	 * @param customerEventIdString
	 */
	public void setCustomerEventIdString(java.lang.String customerEventIdString) {
		this.customerEventIdString = customerEventIdString;
	}

	/**
	 * Gets the downloadedDate value for this EventsInfo.
	 * 
	 * @return downloadedDate
	 */
	public java.util.Calendar getDownloadedDate() {
		return downloadedDate;
	}

	/**
	 * Sets the downloadedDate value for this EventsInfo.
	 * 
	 * @param downloadedDate
	 */
	public void setDownloadedDate(java.util.Calendar downloadedDate) {
		this.downloadedDate = downloadedDate;
	}

	/**
	 * Gets the driverId value for this EventsInfo.
	 * 
	 * @return driverId
	 */
	public java.lang.Long getDriverId() {
		return driverId;
	}

	/**
	 * Sets the driverId value for this EventsInfo.
	 * 
	 * @param driverId
	 */
	public void setDriverId(java.lang.Long driverId) {
		this.driverId = driverId;
	}

	/**
	 * Gets the ERSerialNumber value for this EventsInfo.
	 * 
	 * @return ERSerialNumber
	 */
	public java.lang.String getERSerialNumber() {
		return ERSerialNumber;
	}

	/**
	 * Sets the ERSerialNumber value for this EventsInfo.
	 * 
	 * @param ERSerialNumber
	 */
	public void setERSerialNumber(java.lang.String ERSerialNumber) {
		this.ERSerialNumber = ERSerialNumber;
	}

	/**
	 * Gets the eventId value for this EventsInfo.
	 * 
	 * @return eventId
	 */
	public java.lang.Long getEventId() {
		return eventId;
	}

	/**
	 * Sets the eventId value for this EventsInfo.
	 * 
	 * @param eventId
	 */
	public void setEventId(java.lang.Long eventId) {
		this.eventId = eventId;
	}

	/**
	 * Gets the eventStatusId value for this EventsInfo.
	 * 
	 * @return eventStatusId
	 */
	public java.lang.Long getEventStatusId() {
		return eventStatusId;
	}

	/**
	 * Sets the eventStatusId value for this EventsInfo.
	 * 
	 * @param eventStatusId
	 */
	public void setEventStatusId(java.lang.Long eventStatusId) {
		this.eventStatusId = eventStatusId;
	}

	/**
	 * Gets the eventTypeId value for this EventsInfo.
	 * 
	 * @return eventTypeId
	 */
	public java.lang.Long getEventTypeId() {
		return eventTypeId;
	}

	/**
	 * Sets the eventTypeId value for this EventsInfo.
	 * 
	 * @param eventTypeId
	 */
	public void setEventTypeId(java.lang.Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	/**
	 * Gets the forwardMax value for this EventsInfo.
	 * 
	 * @return forwardMax
	 */
	public java.math.BigDecimal getForwardMax() {
		return forwardMax;
	}

	/**
	 * Sets the forwardMax value for this EventsInfo.
	 * 
	 * @param forwardMax
	 */
	public void setForwardMax(java.math.BigDecimal forwardMax) {
		this.forwardMax = forwardMax;
	}

	/**
	 * Gets the forwardThreshold value for this EventsInfo.
	 * 
	 * @return forwardThreshold
	 */
	public java.math.BigDecimal getForwardThreshold() {
		return forwardThreshold;
	}

	/**
	 * Sets the forwardThreshold value for this EventsInfo.
	 * 
	 * @param forwardThreshold
	 */
	public void setForwardThreshold(java.math.BigDecimal forwardThreshold) {
		this.forwardThreshold = forwardThreshold;
	}

	/**
	 * Gets the groupId value for this EventsInfo.
	 * 
	 * @return groupId
	 */
	public java.lang.Long getGroupId() {
		return groupId;
	}

	/**
	 * Sets the groupId value for this EventsInfo.
	 * 
	 * @param groupId
	 */
	public void setGroupId(java.lang.Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * Gets the heading value for this EventsInfo.
	 * 
	 * @return heading
	 */
	public java.math.BigDecimal getHeading() {
		return heading;
	}

	/**
	 * Sets the heading value for this EventsInfo.
	 * 
	 * @param heading
	 */
	public void setHeading(java.math.BigDecimal heading) {
		this.heading = heading;
	}

	/**
	 * Gets the lastUpdated value for this EventsInfo.
	 * 
	 * @return lastUpdated
	 */
	public java.util.Calendar getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * Sets the lastUpdated value for this EventsInfo.
	 * 
	 * @param lastUpdated
	 */
	public void setLastUpdated(java.util.Calendar lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	/**
	 * Gets the lateralMax value for this EventsInfo.
	 * 
	 * @return lateralMax
	 */
	public java.math.BigDecimal getLateralMax() {
		return lateralMax;
	}

	/**
	 * Sets the lateralMax value for this EventsInfo.
	 * 
	 * @param lateralMax
	 */
	public void setLateralMax(java.math.BigDecimal lateralMax) {
		this.lateralMax = lateralMax;
	}

	/**
	 * Gets the lateralThreshold value for this EventsInfo.
	 * 
	 * @return lateralThreshold
	 */
	public java.math.BigDecimal getLateralThreshold() {
		return lateralThreshold;
	}

	/**
	 * Sets the lateralThreshold value for this EventsInfo.
	 * 
	 * @param lateralThreshold
	 */
	public void setLateralThreshold(java.math.BigDecimal lateralThreshold) {
		this.lateralThreshold = lateralThreshold;
	}

	/**
	 * Gets the latitude value for this EventsInfo.
	 * 
	 * @return latitude
	 */
	public java.math.BigDecimal getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude value for this EventsInfo.
	 * 
	 * @param latitude
	 */
	public void setLatitude(java.math.BigDecimal latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude value for this EventsInfo.
	 * 
	 * @return longitude
	 */
	public java.math.BigDecimal getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude value for this EventsInfo.
	 * 
	 * @param longitude
	 */
	public void setLongitude(java.math.BigDecimal longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the notes value for this EventsInfo.
	 * 
	 * @return notes
	 */
	public java.lang.String getNotes() {
		return notes;
	}

	/**
	 * Sets the notes value for this EventsInfo.
	 * 
	 * @param notes
	 */
	public void setNotes(java.lang.String notes) {
		this.notes = notes;
	}

	/**
	 * Gets the objectRevision value for this EventsInfo.
	 * 
	 * @return objectRevision
	 */
	public java.lang.Integer getObjectRevision() {
		return objectRevision;
	}

	/**
	 * Sets the objectRevision value for this EventsInfo.
	 * 
	 * @param objectRevision
	 */
	public void setObjectRevision(java.lang.Integer objectRevision) {
		this.objectRevision = objectRevision;
	}

	/**
	 * Gets the overdue value for this EventsInfo.
	 * 
	 * @return overdue
	 */
	public java.math.BigDecimal getOverdue() {
		return overdue;
	}

	/**
	 * Sets the overdue value for this EventsInfo.
	 * 
	 * @param overdue
	 */
	public void setOverdue(java.math.BigDecimal overdue) {
		this.overdue = overdue;
	}

	/**
	 * Gets the recordDateTZ value for this EventsInfo.
	 * 
	 * @return recordDateTZ
	 */
	public java.lang.String getRecordDateTZ() {
		return recordDateTZ;
	}

	/**
	 * Sets the recordDateTZ value for this EventsInfo.
	 * 
	 * @param recordDateTZ
	 */
	public void setRecordDateTZ(java.lang.String recordDateTZ) {
		this.recordDateTZ = recordDateTZ;
	}

	/**
	 * Gets the recordDateUTC value for this EventsInfo.
	 * 
	 * @return recordDateUTC
	 */
	public java.util.Calendar getRecordDateUTC() {
		return recordDateUTC;
	}

	/**
	 * Sets the recordDateUTC value for this EventsInfo.
	 * 
	 * @param recordDateUTC
	 */
	public void setRecordDateUTC(java.util.Calendar recordDateUTC) {
		this.recordDateUTC = recordDateUTC;
	}

	/**
	 * Gets the recordDateUtcOffset value for this EventsInfo.
	 * 
	 * @return recordDateUtcOffset
	 */
	public java.lang.Integer getRecordDateUtcOffset() {
		return recordDateUtcOffset;
	}

	/**
	 * Sets the recordDateUtcOffset value for this EventsInfo.
	 * 
	 * @param recordDateUtcOffset
	 */
	public void setRecordDateUtcOffset(java.lang.Integer recordDateUtcOffset) {
		this.recordDateUtcOffset = recordDateUtcOffset;
	}

	/**
	 * Gets the reviewedDate value for this EventsInfo.
	 * 
	 * @return reviewedDate
	 */
	public java.util.Calendar getReviewedDate() {
		return reviewedDate;
	}

	/**
	 * Sets the reviewedDate value for this EventsInfo.
	 * 
	 * @param reviewedDate
	 */
	public void setReviewedDate(java.util.Calendar reviewedDate) {
		this.reviewedDate = reviewedDate;
	}

	/**
	 * Gets the revisionDate value for this EventsInfo.
	 * 
	 * @return revisionDate
	 */
	public java.util.Calendar getRevisionDate() {
		return revisionDate;
	}

	/**
	 * Sets the revisionDate value for this EventsInfo.
	 * 
	 * @param revisionDate
	 */
	public void setRevisionDate(java.util.Calendar revisionDate) {
		this.revisionDate = revisionDate;
	}

	/**
	 * Gets the score value for this EventsInfo.
	 * 
	 * @return score
	 */
	public java.lang.Long getScore() {
		return score;
	}

	/**
	 * Sets the score value for this EventsInfo.
	 * 
	 * @param score
	 */
	public void setScore(java.lang.Long score) {
		this.score = score;
	}

	/**
	 * Gets the shockThreshold value for this EventsInfo.
	 * 
	 * @return shockThreshold
	 */
	public java.math.BigDecimal getShockThreshold() {
		return shockThreshold;
	}

	/**
	 * Sets the shockThreshold value for this EventsInfo.
	 * 
	 * @param shockThreshold
	 */
	public void setShockThreshold(java.math.BigDecimal shockThreshold) {
		this.shockThreshold = shockThreshold;
	}

	/**
	 * Gets the speed value for this EventsInfo.
	 * 
	 * @return speed
	 */
	public java.math.BigDecimal getSpeed() {
		return speed;
	}

	/**
	 * Sets the speed value for this EventsInfo.
	 * 
	 * @param speed
	 */
	public void setSpeed(java.math.BigDecimal speed) {
		this.speed = speed;
	}

	/**
	 * Gets the vehicleId value for this EventsInfo.
	 * 
	 * @return vehicleId
	 */
	public java.lang.Long getVehicleId() {
		return vehicleId;
	}

	/**
	 * Sets the vehicleId value for this EventsInfo.
	 * 
	 * @param vehicleId
	 */
	public void setVehicleId(java.lang.Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	private java.lang.Object __equalsCalc = null;

	@SuppressWarnings("unused")
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof EventsInfo))
			return false;
		EventsInfo other = (EventsInfo) obj;
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
		        && ((this.behaviors == null && other.getBehaviors() == null)
		                || (this.behaviors != null && java.util.Arrays.equals(this.behaviors, other.getBehaviors())))
		        && ((this.coachId == null && other.getCoachId() == null)
		                || (this.coachId != null && this.coachId.equals(other.getCoachId())))
		        && ((this.creationDate == null && other.getCreationDate() == null)
		                || (this.creationDate != null && this.creationDate.equals(other.getCreationDate())))
		        && ((this.customerEventId == null && other.getCustomerEventId() == null)
		                || (this.customerEventId != null && this.customerEventId.equals(other.getCustomerEventId())))
		        && ((this.customerEventIdString == null && other.getCustomerEventIdString() == null)
		                || (this.customerEventIdString != null
		                        && this.customerEventIdString.equals(other.getCustomerEventIdString())))
		        && ((this.downloadedDate == null && other.getDownloadedDate() == null)
		                || (this.downloadedDate != null && this.downloadedDate.equals(other.getDownloadedDate())))
		        && ((this.driverId == null && other.getDriverId() == null)
		                || (this.driverId != null && this.driverId.equals(other.getDriverId())))
		        && ((this.ERSerialNumber == null && other.getERSerialNumber() == null)
		                || (this.ERSerialNumber != null && this.ERSerialNumber.equals(other.getERSerialNumber())))
		        && ((this.eventId == null && other.getEventId() == null)
		                || (this.eventId != null && this.eventId.equals(other.getEventId())))
		        && ((this.eventStatusId == null && other.getEventStatusId() == null)
		                || (this.eventStatusId != null && this.eventStatusId.equals(other.getEventStatusId())))
		        && ((this.eventTypeId == null && other.getEventTypeId() == null)
		                || (this.eventTypeId != null && this.eventTypeId.equals(other.getEventTypeId())))
		        && ((this.forwardMax == null && other.getForwardMax() == null)
		                || (this.forwardMax != null && this.forwardMax.equals(other.getForwardMax())))
		        && ((this.forwardThreshold == null && other.getForwardThreshold() == null)
		                || (this.forwardThreshold != null && this.forwardThreshold.equals(other.getForwardThreshold())))
		        && ((this.groupId == null && other.getGroupId() == null)
		                || (this.groupId != null && this.groupId.equals(other.getGroupId())))
		        && ((this.heading == null && other.getHeading() == null)
		                || (this.heading != null && this.heading.equals(other.getHeading())))
		        && ((this.lastUpdated == null && other.getLastUpdated() == null)
		                || (this.lastUpdated != null && this.lastUpdated.equals(other.getLastUpdated())))
		        && ((this.lateralMax == null && other.getLateralMax() == null)
		                || (this.lateralMax != null && this.lateralMax.equals(other.getLateralMax())))
		        && ((this.lateralThreshold == null && other.getLateralThreshold() == null)
		                || (this.lateralThreshold != null && this.lateralThreshold.equals(other.getLateralThreshold())))
		        && ((this.latitude == null && other.getLatitude() == null)
		                || (this.latitude != null && this.latitude.equals(other.getLatitude())))
		        && ((this.longitude == null && other.getLongitude() == null)
		                || (this.longitude != null && this.longitude.equals(other.getLongitude())))
		        && ((this.notes == null && other.getNotes() == null)
		                || (this.notes != null && this.notes.equals(other.getNotes())))
		        && ((this.objectRevision == null && other.getObjectRevision() == null)
		                || (this.objectRevision != null && this.objectRevision.equals(other.getObjectRevision())))
		        && ((this.overdue == null && other.getOverdue() == null)
		                || (this.overdue != null && this.overdue.equals(other.getOverdue())))
		        && ((this.recordDateTZ == null && other.getRecordDateTZ() == null)
		                || (this.recordDateTZ != null && this.recordDateTZ.equals(other.getRecordDateTZ())))
		        && ((this.recordDateUTC == null && other.getRecordDateUTC() == null)
		                || (this.recordDateUTC != null && this.recordDateUTC.equals(other.getRecordDateUTC())))
		        && ((this.recordDateUtcOffset == null && other.getRecordDateUtcOffset() == null)
		                || (this.recordDateUtcOffset != null
		                        && this.recordDateUtcOffset.equals(other.getRecordDateUtcOffset())))
		        && ((this.reviewedDate == null && other.getReviewedDate() == null)
		                || (this.reviewedDate != null && this.reviewedDate.equals(other.getReviewedDate())))
		        && ((this.revisionDate == null && other.getRevisionDate() == null)
		                || (this.revisionDate != null && this.revisionDate.equals(other.getRevisionDate())))
		        && ((this.score == null && other.getScore() == null)
		                || (this.score != null && this.score.equals(other.getScore())))
		        && ((this.shockThreshold == null && other.getShockThreshold() == null)
		                || (this.shockThreshold != null && this.shockThreshold.equals(other.getShockThreshold())))
		        && ((this.speed == null && other.getSpeed() == null)
		                || (this.speed != null && this.speed.equals(other.getSpeed())))
		        && ((this.vehicleId == null && other.getVehicleId() == null)
		                || (this.vehicleId != null && this.vehicleId.equals(other.getVehicleId())));
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
		if (getBehaviors() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getBehaviors()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getBehaviors(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getCoachId() != null) {
			_hashCode += getCoachId().hashCode();
		}
		if (getCreationDate() != null) {
			_hashCode += getCreationDate().hashCode();
		}
		if (getCustomerEventId() != null) {
			_hashCode += getCustomerEventId().hashCode();
		}
		if (getCustomerEventIdString() != null) {
			_hashCode += getCustomerEventIdString().hashCode();
		}
		if (getDownloadedDate() != null) {
			_hashCode += getDownloadedDate().hashCode();
		}
		if (getDriverId() != null) {
			_hashCode += getDriverId().hashCode();
		}
		if (getERSerialNumber() != null) {
			_hashCode += getERSerialNumber().hashCode();
		}
		if (getEventId() != null) {
			_hashCode += getEventId().hashCode();
		}
		if (getEventStatusId() != null) {
			_hashCode += getEventStatusId().hashCode();
		}
		if (getEventTypeId() != null) {
			_hashCode += getEventTypeId().hashCode();
		}
		if (getForwardMax() != null) {
			_hashCode += getForwardMax().hashCode();
		}
		if (getForwardThreshold() != null) {
			_hashCode += getForwardThreshold().hashCode();
		}
		if (getGroupId() != null) {
			_hashCode += getGroupId().hashCode();
		}
		if (getHeading() != null) {
			_hashCode += getHeading().hashCode();
		}
		if (getLastUpdated() != null) {
			_hashCode += getLastUpdated().hashCode();
		}
		if (getLateralMax() != null) {
			_hashCode += getLateralMax().hashCode();
		}
		if (getLateralThreshold() != null) {
			_hashCode += getLateralThreshold().hashCode();
		}
		if (getLatitude() != null) {
			_hashCode += getLatitude().hashCode();
		}
		if (getLongitude() != null) {
			_hashCode += getLongitude().hashCode();
		}
		if (getNotes() != null) {
			_hashCode += getNotes().hashCode();
		}
		if (getObjectRevision() != null) {
			_hashCode += getObjectRevision().hashCode();
		}
		if (getOverdue() != null) {
			_hashCode += getOverdue().hashCode();
		}
		if (getRecordDateTZ() != null) {
			_hashCode += getRecordDateTZ().hashCode();
		}
		if (getRecordDateUTC() != null) {
			_hashCode += getRecordDateUTC().hashCode();
		}
		if (getRecordDateUtcOffset() != null) {
			_hashCode += getRecordDateUtcOffset().hashCode();
		}
		if (getReviewedDate() != null) {
			_hashCode += getReviewedDate().hashCode();
		}
		if (getRevisionDate() != null) {
			_hashCode += getRevisionDate().hashCode();
		}
		if (getScore() != null) {
			_hashCode += getScore().hashCode();
		}
		if (getShockThreshold() != null) {
			_hashCode += getShockThreshold().hashCode();
		}
		if (getSpeed() != null) {
			_hashCode += getSpeed().hashCode();
		}
		if (getVehicleId() != null) {
			_hashCode += getVehicleId().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
	        EventsInfo.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventsInfo"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("behaviors");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Behaviors"));
		elemField.setXmlType(new javax.xml.namespace.QName(
		        "http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api",
		        "EventBehavior"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		elemField.setItemQName(new javax.xml.namespace.QName(
		        "http://schemas.datacontract.org/2004/07/DriveCam.HindSight.Messaging.Messages.MessageClasses.Api",
		        "EventBehavior"));
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("coachId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "CoachId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("creationDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "CreationDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("customerEventId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "CustomerEventId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("customerEventIdString");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "CustomerEventIdString"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("downloadedDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "DownloadedDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("driverId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "DriverId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("ERSerialNumber");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ERSerialNumber"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("eventId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("eventStatusId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventStatusId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("eventTypeId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "EventTypeId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("forwardMax");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ForwardMax"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("forwardThreshold");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ForwardThreshold"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("groupId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "GroupId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("heading");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Heading"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("lastUpdated");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "LastUpdated"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("lateralMax");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "LateralMax"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("lateralThreshold");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "LateralThreshold"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("latitude");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Latitude"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("longitude");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Longitude"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("notes");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Notes"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("objectRevision");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ObjectRevision"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("overdue");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Overdue"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("recordDateTZ");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "RecordDateTZ"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("recordDateUTC");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "RecordDateUTC"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("recordDateUtcOffset");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "RecordDateUtcOffset"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("reviewedDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ReviewedDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("revisionDate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "RevisionDate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("score");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Score"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("shockThreshold");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "ShockThreshold"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("speed");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "Speed"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("vehicleId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://DriveCam.com/Classes/", "VehicleId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
