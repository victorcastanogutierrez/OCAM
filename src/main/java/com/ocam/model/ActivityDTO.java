package com.ocam.model;

/**
 * Clase que encapsula la información para pasar datos de actividades entre
 * capas de la aplicación
 * 
 * @author Victor
 *
 */
public class ActivityDTO {

	private Integer maxResults;
	private Integer minResults;

	public ActivityDTO(Integer maxResults, Integer minResults) {
		super();
		this.maxResults = maxResults;
		this.minResults = minResults;
	}

	public ActivityDTO() {
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
}
