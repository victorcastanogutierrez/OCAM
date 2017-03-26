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
public class SaveHiker {

	private HikerRepository hikerRepository;

	@Autowired
	public SaveHiker(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(HikerDTO hiker) throws BusinessException {
		assertHikerExists(hiker);

		String passwd = hiker.getPassword();
		String codedPassword = encryptPassword(passwd);
		if (assertCodedPassword(codedPassword)) {
			throw new BusinessException(
					"Error en la encriptación de la contraseña del usuario "
							+ hiker.getUsername());
		}

		Hiker h = getNewHiker(hiker, codedPassword);
		this.hikerRepository.save(h);
	}

	private Hiker getNewHiker(HikerDTO hiker, String codedPassword) {
		Hiker h = new Hiker();
		h.setEmail(hiker.getEmail());
		h.setLogin(hiker.getUsername());
		h.setPassword(codedPassword);
		return h;
	}

	private boolean assertCodedPassword(String codedPassword) {
		return codedPassword == null;
	}

	private void assertHikerExists(HikerDTO hiker) throws BusinessException {
		if (hiker == null) {
			throw new BusinessException("Datos de hiker invalidos");
		}

		Hiker eHiker = hikerRepository
				.findTopByLoginOrEmail(hiker.getUsername(), hiker.getEmail());

		if (eHiker != null) {
			if (eHiker.getEmail().equals(hiker.getEmail())) {
				throw new BusinessException("El email " + hiker.getEmail()
						+ " ya está en uso por otro usuario registrado.");
			}

			if (eHiker.getLogin().equals(hiker.getUsername())) {
				throw new BusinessException("El nombre de usuario "
						+ hiker.getUsername()
						+ " ya está en uso por otro usuario registrado.");
			}
		}
	}

	private String encryptPassword(String passwd) throws BusinessException {
		try {
			return MD5Util.MD5(passwd);
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(
					"Ocurrió un error guardando el nuevo usuario");
		}
	}
}
