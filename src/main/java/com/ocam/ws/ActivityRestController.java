package com.ocam.ws;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.ActivityHikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.ActivityService;
import com.ocam.util.ApiError;

@RestController
public class ActivityRestController {

	@Autowired
	private ActivityService activityService;

	/**
	 * Devuelve las actividades pendientes encontradas entre los rangos
	 * indicados por parámetro
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pendingActivities/{minResults}/{maxResults}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> pendingActivities(
			@PathVariable("minResults") Integer minResults,
			@PathVariable("maxResults") Integer maxResults) {

		List<Activity> acts;
		try {
			acts = activityService.findAllPendingActivities(
					new ActivityDTO(maxResults, minResults));
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(acts, HttpStatus.OK);
	}

	@RequestMapping(value = "/countPendingActivities",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Object> countPendingActivities() {

		List<Activity> acts;
		try {
			acts = activityService.findAllPendingActivities(
					new ActivityDTO(Integer.MAX_VALUE, 0));
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(acts.size(), HttpStatus.OK);
	}

	@RequestMapping(value = "/api/activity/save", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(
			@RequestBody ActivityHikerDTO activityHiker) {
		Activity result = null;
		try {
			result = activityService.saveActivity(activityHiker);
		} catch (BusinessException e) {

			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(result, HttpStatus.CREATED);
	}

}
