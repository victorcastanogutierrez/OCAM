package com.ocam.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GPSPoint {

	@Column (name="LATITUDE")
	private float latitude;
	
	@Column (name="LONGITUDE")
	private float longitude;
}
