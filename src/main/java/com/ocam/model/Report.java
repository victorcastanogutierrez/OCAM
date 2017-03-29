package com.ocam.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ocam.model.types.GPSPoint;

@Table(name = "REPORTS")
@Entity
public class Report extends BaseEntity implements Comparable<Report> {

	@Column(name = "DATE")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne
	@JoinColumn(name = "ACTIVITY_ID")
	@NotNull
	@JsonIgnore
	private Activity activity;

	@ManyToOne
	@JoinColumn(name = "HIKER_ID")
	@NotNull
	private Hiker hiker;

	@Embedded
	@NotNull
	private GPSPoint point;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Hiker getHiker() {
		return hiker;
	}

	public void setHiker(Hiker hiker) {
		this.hiker = hiker;
	}

	public GPSPoint getPoint() {
		return point;
	}

	public void setPoint(GPSPoint point) {
		this.point = point;
	}

	@Override
	public int compareTo(Report o) {
		return this.date.compareTo(o.getDate());
	}

}
