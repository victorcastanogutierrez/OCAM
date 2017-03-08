package com.ocam.ws;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Activity;
import com.ocam.service.ActivityService;

@RestController
public class ActivityRestController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping(value = "/pendingActivities", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> pendingActivities() {
		Set<Activity> acts = activityService.findAllPendingActivities();
		return new ResponseEntity<>(acts, HttpStatus.OK);
	}
}
