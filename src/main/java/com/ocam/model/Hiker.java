package com.ocam.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "HIKERS")
@Entity
public class Hiker extends BaseEntity {

	@Column(name = "EMAIL_ADDRESS", unique = true)
	@NotNull
	private String email;

	@Column(name = "LOGIN", unique = true)
	@NotNull
	private String login;

	@Column(name = "PASSWORD")
	@NotNull
	@JsonIgnore
	private String password;

	@Column(name = "ACTIVE")
	@NotNull
	@JsonIgnore
	private Boolean active = Boolean.FALSE;

	@Column(name = "DELETED")
	@NotNull
	@JsonIgnore
	private Boolean deleted = Boolean.FALSE;

	@Column(name = "ACTIVE_CODE")
	@JsonIgnore
	private String activeCode;

	@ManyToMany(mappedBy = "hikers")
	@JsonIgnore
	private Set<Activity> activities = new HashSet<Activity>();

	@ManyToMany(mappedBy = "guides")
	@JsonIgnore
	private Set<Activity> activityGuide = new HashSet<Activity>();

	@OneToMany(mappedBy = "hiker")
	@JsonIgnore
	private Set<Report> reports = new HashSet<Report>();

	@OneToMany(mappedBy = "owner")
	@JsonIgnore
	private Set<Activity> owneds = new HashSet<Activity>();

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getActiveCode() {
		return activeCode;
	}

	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	public Set<Activity> getActivities() {
		return activities;
	}

	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}

	public Set<Activity> getActivityGuide() {
		return activityGuide;
	}

	public void setActivityGuide(Set<Activity> activityGuide) {
		this.activityGuide = activityGuide;
	}

	public Set<Report> getReports() {
		return reports;
	}

	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

	public Set<Activity> getOwneds() {
		return owneds;
	}

	public void setOwneds(Set<Activity> owneds) {
		this.owneds = owneds;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}
