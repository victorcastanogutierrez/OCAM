package com.ocam.service.impl.hiker;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;
import com.ocam.util.MD5Util;

@Component
public class FindHikerByLoginPassword {

	private HikerRepository hikerRepository;

	@Autowired
	public FindHikerByLoginPassword(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Hiker execute(String login, String password)
			throws BusinessException {
		if (!assertPasswordAndLogin(login, password)) {
			throw new BusinessException("Login y password son requeridos");
		}
		String codedPassword;
		try {
			codedPassword = MD5Util.MD5(password);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(
					"Error codificando la password en usuario " + login);
		}
		Hiker hiker = this.hikerRepository.findByLoginAndPassword(login,
				codedPassword);

		if (hiker != null && !assertCuentaActiva(hiker)) {
			throw new BusinessException(
					"La cuenta aún no ha sido activdada! " + login);
		}
		return hiker;
	}

	private boolean assertCuentaActiva(Hiker hiker) {
		return hiker.getActive() && !hiker.getDeleted();
	}

	private boolean assertPasswordAndLogin(String login, String password) {
		return login != null && password != null;
	}

}
