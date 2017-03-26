package com.ocam.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ocam.model.types.ActivityStatus;

@Entity

@Table(name = "ACTIVITIES")
public class Activity extends BaseEntity {

	@Column(name = "SHORT_DESCRIPTION")
	@NotNull
	private String shortDescription;

	@Column(name = "LONG_DESCRIPTION")
	@NotNull
	private String longDescription;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date startDate;

	@Column(name = "MAX_PLACES")
	private Long maxPlaces;

	@Column(name = "JOIN_PASSWORD")
	@JsonIgnore
	private String password;

	@Column(name = "TRACK", columnDefinition = "LONGBLOB")
	@NotNull
	private String track;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "STATUS")
	private ActivityStatus status;

	@NotNull
	@Column(name = "DELETED")
	private Boolean deleted = Boolean.FALSE;

	@ManyToOne
	@JoinColumn(name = "OWNER_ID")
	private Hiker owner;

	@ManyToMany
	@JoinTable(name = "ACTIVITY_HIKERS")
	private Set<Hiker> hikers = new HashSet<Hiker>();

	@ManyToMany
	@JoinTable(name = "ACTIVITY_GUIDES")
	private Set<Hiker> guides = new HashSet<Hiker>();

	@OneToMany(mappedBy = "activity")
	private Set<Report> reports = new HashSet<Report>();

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Hiker getOwner() {
		return owner;
	}

	public void setOwner(Hiker owner) {
		this.owner = owner;
	}

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
	}

	public Set<Report> getReports() {
		return reports;
	}

	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getMaxPlaces() {
		return maxPlaces;
	}

	public void setMaxPlaces(Long maxPlaces) {
		this.maxPlaces = maxPlaces;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public Set<Hiker> getHikers() {
		return hikers;
	}

	public void setHikers(Set<Hiker> hikers) {
		this.hikers = hikers;
	}

	public Set<Hiker> getGuides() {
		return guides;
	}

	public void setGuides(Set<Hiker> guides) {
		this.guides = guides;
	}

}
