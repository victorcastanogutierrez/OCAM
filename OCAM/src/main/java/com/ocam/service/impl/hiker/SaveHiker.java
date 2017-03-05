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
public class SaveHiker {

	private HikerRepository hikerRepository;

	@Autowired
	public SaveHiker(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Hiker hiker) throws BusinessException {
		assertHikerExists(hiker);

		String passwd = hiker.getPassword();
		String codedPassword = encryptPassword(passwd);
		if (codedPassword == null) {
			throw new BusinessException(
					"Error en la encriptación de la contraseña del usuario "
							+ hiker.getLogin());
		}
		hiker.setPassword(codedPassword);

		this.hikerRepository.save(hiker);
	}

	private void assertHikerExists(Hiker hiker) throws BusinessException {
		Hiker eHiker = hikerRepository.findTopByLoginOrEmail(hiker.getLogin(),
				hiker.getEmail());

		if (eHiker.getEmail().equals(hiker.getEmail())) {
			throw new BusinessException("El email " + hiker.getEmail()
					+ " ya está en uso por otro usuario registrado.");
		}

		if (eHiker.getLogin().equals(hiker.getLogin())) {
			throw new BusinessException(
					"El nombre de usuario " + hiker.getLogin()
							+ " ya está en uso por otro usuario registrado.");
		}

	}

	private String encryptPassword(String passwd) {
		try {
			return MD5Util.MD5(passwd);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
