package com.ocam.ws;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.ActivityService;
import com.ocam.ws.auth.util.ApiError;
import com.ocam.ws.auth.util.UserVerifierUtils;

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

	/**
	 * Retorna el número total de actividades pendientes
	 * 
	 * @return
	 */
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

	/**
	 * Guarda y actualiza una actividad
	 * 
	 * @param request
	 * @param activityHiker
	 * @return
	 */
	@RequestMapping(value = "/api/activity/save", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> save(HttpServletRequest request,
			@RequestBody ActivityHikerDTO activityHiker) {

		String user = UserVerifierUtils.getRequestUsername(request);
		activityHiker.setRequestUser(user);

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

	/**
	 * Comprueba la password de una actividad
	 * 
	 * @param request
	 * @param activityHiker
	 * @return
	 */
	@RequestMapping(
			value = "/api/activity/checkPassword/{activityId}/{password}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> checkActivityPassword(
			@PathVariable("activityId") Long id,
			@PathVariable("password") String password) {

		ActivityDTO act = new ActivityDTO();
		act.setPassword(password);
		act.setId(id);

		try {
			activityService.checkActivityPassword(act);
		} catch (BusinessException e) {

			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * Devuelve una actividad buscada por ID
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/activity/{activityId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> find(@PathVariable("activityId") Long id) {

		Activity result = activityService.findActivityById(id);
		if (result == null) {
			return new ResponseEntity<Object>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	/**
	 * Devuelve los últimos reportes de cada hiker de una actividad
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/lastActivityReports/{activityId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findLastActivityReports(
			@PathVariable("activityId") Long id) {

		Set<Report> result;
		try {
			result = activityService.findLastActivityReports(id);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Método que busca los reports de un hiker dado para una actividad dada
	 * 
	 * @param activityId
	 * @param hikerId
	 * @return
	 */
	@RequestMapping(
			value = "/api/activityHikerReports/{activityId}/{hikerEmail:.+}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findActivityHikerReports(
			@PathVariable("activityId") Long activityId,
			@PathVariable("hikerEmail") String hikerEmail) {

		Set<Report> reports = activityService
				.findActivityReportsByHiker(activityId, hikerEmail);
		if (reports == null) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<>(reports, HttpStatus.OK);
	}

	/**
	 * Devuelve las actividades pendientes de realización o bien que estén en
	 * curso
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api/findAllPendingRunningActivities",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> allPendingActivities() {

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
		return new ResponseEntity<>(acts, HttpStatus.OK);
	}

	/**
	 * Da una actividad por comenzada cambiándole el estado a RUNNING
	 * 
	 * @param request
	 * @param activityDTO
	 * @return
	 */
	@RequestMapping(value = "/api/startActivity", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> startActivity(HttpServletRequest request,
			@RequestBody Activity activityDTO) {

		String user = UserVerifierUtils.getRequestUsername(request);
		try {
			activityService.startActivity(activityDTO.getId(),
					activityDTO.getPassword(), user);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Actualiza la password de una actividad
	 * 
	 * @param request
	 * @param activityId
	 * @param password
	 * @return
	 */
	@RequestMapping(
			value = "/api/updateActivityPassword/{activityId}/{password}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateActivityPassword(HttpServletRequest request,
			@PathVariable("activityId") Long activityId,
			@PathVariable("password") String password) {

		String user = UserVerifierUtils.getRequestUsername(request);
		ActivityHikerDTO act = new ActivityHikerDTO();
		act.setActivity(new Activity());
		act.getActivity().setId(activityId);
		act.getActivity().setPassword(password);
		act.setRequestUser(user);

		try {
			activityService.updateActivityPassword(act);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (BusinessException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Incluye a un hiker como participante de una actividad
	 * 
	 * @param activityId
	 * @param login
	 * @return
	 */
	@RequestMapping(value = "/api/joinActivity/{activityId}/{login}/{password}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> joinActivity(
			@PathVariable("activityId") Long activityId,
			@PathVariable("login") String login,
			@PathVariable("password") String password) {

		try {
			activityService.joinActivityHiker(activityId, login, password);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Retorna todos los Hiker asociados a una actividad, sean guías o no
	 * 
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/api/findActivityHikers/{activityId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findActivityHikers(
			@PathVariable("activityId") Long activityId) {

		List<Hiker> result;
		try {
			result = activityService.findActivityHikers(activityId);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Cierra una actividad pasándola a estado CLOSED
	 * 
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/api/closeActivity/{activityId}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> closeActivity(
			@PathVariable("activityId") Long activityId) {

		try {
			activityService.closeActivity(activityId);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Elimina un hiker de una actividad.
	 * 
	 * @param activityId
	 * @param login
	 * @return
	 */
	@RequestMapping(value = "/api/activity/leaveActivity/{activityId}/{login}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> leaveActivity(
			@PathVariable("activityId") Long activityId,
			@PathVariable("login") String login) {

		try {
			activityService.deleteActivityHiker(activityId, login);
		} catch (BusinessException e) {

			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
