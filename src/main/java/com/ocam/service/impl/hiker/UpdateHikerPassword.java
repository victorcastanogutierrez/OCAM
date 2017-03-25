package com.ocam.service.impl.hiker;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;
import com.ocam.util.MD5Util;

@Component
public class UpdateHikerPassword {

	private HikerRepository hikerRepository;

	@Autowired
	public UpdateHikerPassword(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(HikerDTO hikerdto) throws BusinessException {

		if (!assertHiker(hikerdto)) {
			throw new BusinessException("Datos de Hiker incorrectos");
		}

		String encryptPassword = encryptPassword(hikerdto.getPassword());
		Hiker hiker = hikerRepository.findByLoginAndPassword(
				hikerdto.getUsername(), encryptPassword);

		if (!assertPasswordActual(hiker)) {
			throw new BusinessException("Password actual incorrecta");
		}

		if (!assertHikerPermissions(hikerdto, hiker)) {
			throw new BusinessException(
					"Error de permisos cambiando la contraseña");
		}

		String newPassword = encryptPassword(hikerdto.getNewPassword());
		hiker.setPassword(newPassword);
	}

	/**
	 * Comprueba que el usuario asociado al token con el que viene la petición
	 * coincide con el usuario que pretende cambiar su password
	 * 
	 * @param hikerdto
	 * @param hiker
	 * @return
	 */
	private boolean assertHikerPermissions(HikerDTO hikerdto, Hiker hiker) {
		return hikerdto.getRequestUser().equals(hiker.getLogin());
	}

	private boolean assertPasswordActual(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertHiker(HikerDTO hikerdto) {
		return hikerdto != null;
	}

	private String encryptPassword(String passwd) throws BusinessException {
		try {
			return MD5Util.MD5(passwd);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(
					"Ocurrió un error actualizando la clave del usuario");
		}
	}

}
