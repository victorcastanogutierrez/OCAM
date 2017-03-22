package com.ocam.model;

import java.util.Date;

/**
 * Clase que encapsula la información para pasar datos de actividades entre
 * capas de la aplicación
 * 
 * @author Victor
 *
 */
public class ActivityDTO {

	private String shortDescription;
	private String longDescription;
	private Date startDate;
	private Long maxPlaces;

	private Integer maxResults;
	private Integer minResults;
	private String track;

	public ActivityDTO(Integer maxResults, Integer minResults) {
		super();
		this.maxResults = maxResults;
		this.minResults = minResults;
	}

	public ActivityDTO() {
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

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public void setMinResults(Integer minResults) {
		this.minResults = minResults;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public Integer getMinResults() {
		return minResults;
	}

	public void setMinResults(int minResults) {
		this.minResults = minResults;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}
}
