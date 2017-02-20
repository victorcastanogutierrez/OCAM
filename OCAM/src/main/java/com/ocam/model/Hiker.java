package com.ocam.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
	private String password;

	@ManyToMany(mappedBy = "hikers")
	private Set<Activity> activities = new HashSet<Activity>();

	@ManyToMany(mappedBy = "guides")
	private Set<Activity> activityGuide = new HashSet<Activity>();

	@OneToMany(mappedBy = "hiker")
	private Set<Report> reports = new HashSet<Report>();

	public Set<Report> getReports() {
		return reports;
	}

	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

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
}
