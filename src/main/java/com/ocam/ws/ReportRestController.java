package com.ocam.ws;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.ReportService;
import com.ocam.ws.auth.util.ApiError;

@RestController
public class ReportRestController {

	@Autowired
	private ReportService reportService;

	/**
	 * Retorna el objeto Hiker buscándolo por el login
	 * 
	 * @param username
	 *            login del hiker que se quiere buscar
	 * @return objeto Hiker codificado en JSON. Vacío en caso de no encontrarse
	 */
	@RequestMapping(value = "/api/report/save", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@RequestBody Report report) {
		try {
			reportService.save(report);
		} catch (BusinessException e) {

			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * Devuelve el último reporte de un hiker de una actividad
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(
			value = "/api/findLastHikerActivityReport/{activityId}/{login}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findLastHikerActivityReport(
			@PathVariable("activityId") Long id,
			@PathVariable("login") String login) {

		Set<Report> result;
		try {
			result = reportService.findHikerLastActivityReport(id, login);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * Devuelve todos los reportes de una actividad
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/findAllByActivity/{activityId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findAllByActivity(
			@PathVariable("activityId") Long id) {

		Set<Report> result;
		try {
			result = reportService.findAllByActivity(id);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
