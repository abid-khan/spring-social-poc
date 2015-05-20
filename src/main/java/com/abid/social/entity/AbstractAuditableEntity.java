package com.abid.social.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.abid.social.constant.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author abidk
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractAuditableEntity extends AbstractPersistable<Long> {

	@JsonIgnore
	@Version
	@Column(name = "version")
	protected Long version;

	@NotNull
	@Enumerated(EnumType.STRING)
	protected Status status;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	protected DateTime createdOn;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	protected DateTime lastModifiedOn;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public DateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(DateTime createdOn) {
		this.createdOn = createdOn;
	}

	public DateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(DateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@PrePersist
	public void onCreate() {
		this.createdOn = new DateTime();
		this.status = Status.ACTIVE;
	}

	@PreUpdate
	public void onUpdate() {
		this.lastModifiedOn = new DateTime();
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return super.isNew();
	}

}
