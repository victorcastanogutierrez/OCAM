package com.ocam.ws;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.HikerService;
import com.ocam.util.ApiError;

@RestController
public class HikerRestController {

	@Autowired
	private HikerService hikerService;

	/**
	 * Retorna el objeto Hiker buscándolo por el login
	 * 
	 * @param username
	 *            login del hiker que se quiere buscar
	 * @return objeto Hiker codificado en JSON. Vacío en caso de no encontrarse
	 */
	@RequestMapping(value = "/api/hiker/{username}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Hiker> findHiker(
			@PathVariable("username") String username) {
		Hiker hiker = hikerService.findHikerByLogin(username);
		if (hiker == null) {
			return new ResponseEntity<Hiker>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Hiker>(hiker, HttpStatus.OK);
	}

	/**
	 * Guarda un nuevo Hiker en el sistema
	 * 
	 * @param hiker
	 *            Objeto Hiker a guardar
	 * @param ucBuilder
	 *            builder para redireccionar
	 * @return Código OK en caso de registrar el nuevo hiker
	 */
	@RequestMapping(value = "/hiker", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> saveHiker(@RequestBody Hiker hiker,
			UriComponentsBuilder ucBuilder) {
		try {
			hikerService.saveHiker(hiker);
		} catch (BusinessException e) {

			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(HttpStatus.CREATED);
	}

	/**
	 * Método para cambiar la password de un hiker
	 * 
	 * @param hiker
	 *            DTO con los datos del hiker y passwords
	 * @return
	 */
	@RequestMapping(value = "/api/hiker/changePassword",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> verifyPassword(@RequestBody HikerDTO hiker) {

		try {
			hikerService.changePassword(hiker);
		} catch (BusinessException e) {
			ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY,
					e.getMessage());
			return new ResponseEntity<Object>(apiError, new HttpHeaders(),
					apiError.getStatus());
		}
		return new ResponseEntity<Object>(HttpStatus.OK);

	}

	/**
	 * Comprueba que un correo electrónico exista en la aplicación
	 * 
	 * @param email
	 *            email del hiker que se quiere buscar
	 * @return 200 en caso de encontrarse, 422 en caso contrario
	 */
	@RequestMapping(value = "/api/existshiker/{email:.+}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> findHikerByEmail(
			@PathVariable("email") String email) {
		Hiker hiker = hikerService.findHikerByEmail(email);
		if (hiker == null) {
			return new ResponseEntity<Hiker>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<Hiker>(HttpStatus.OK);
	}
}
