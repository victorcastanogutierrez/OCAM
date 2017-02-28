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
		if (assertHikerExists(hiker)) {
			throw new BusinessException("Hiker con login " + hiker.getLogin()
					+ " email " + hiker.getEmail() + " ya existe.");
		}

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

	private boolean assertHikerExists(Hiker hiker) {
		return hikerRepository.findByLoginOrEmail(hiker.getLogin(),
				hiker.getEmail()) != null ? true : false;
	}

	private String encryptPassword(String passwd) {
		try {
			return MD5Util.MD5(passwd);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
