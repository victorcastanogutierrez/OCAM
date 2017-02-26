package com.ocam.ws;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Activity;

@RestController
public class ActivityRestController {

	@RequestMapping(value = "/testing", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Activity> greeting(
			@RequestParam(value = "name") String name) {
		Activity act = new Activity();
		act.setTrack(name);
		return new ResponseEntity<Activity>(act, HttpStatus.OK);
	}
}
