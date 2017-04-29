package com.ocam.service.impl.hiker;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;
import com.ocam.util.MD5Util;
import com.ocam.util.MailUtils;

@Component
public class ResetHikerPassword {

	private HikerRepository hikerRepository;
	private SecureRandom random = new SecureRandom();

	@Autowired
	public ResetHikerPassword(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(String email) throws BusinessException {

		if (!assertEmailNotNull(email)) {
			throw new BusinessException("Email inválido");
		}

		Hiker h = hikerRepository.findByEmail(email);
		if (!assertValidHiker(h)) {
			throw new BusinessException("Hiker no existente");
		}

		String newPassword = new BigInteger(130, random).toString(32);
		String encryptedNewPassword = encryptPassword(newPassword);

		if (!assertCodedPassword(encryptedNewPassword)) {
			throw new BusinessException(
					"Error en la encriptación de la nueva contraseña del usuario ");
		}

		h.setPassword(encryptedNewPassword);
		MailUtils.sendEmail(email, "Nueva contraseña OCAM",
				"Hola " + h.getLogin() + "\n"
						+ "Has solicitado una nueva contraseña. La contraseña nueva asignada a la cuenta es: "
						+ newPassword);
	}

	private boolean assertCodedPassword(String password) {
		return password != null;
	}

	private boolean assertEmailNotNull(String email) {
		return email != null;
	}

	private boolean assertValidHiker(Hiker h) {
		return h != null && !h.getDeleted() && h.getActive();
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
