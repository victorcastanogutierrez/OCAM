package com.ocam.model.types;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GPSPoint {

	@Column(name = "LATITUDE")
	private float latitude;

	@Column(name = "LONGITUDE")
	private float longitude;

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
}
