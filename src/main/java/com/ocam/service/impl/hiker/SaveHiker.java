package com.ocam.service.impl.hiker;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;
import com.ocam.util.MD5Util;
import com.ocam.util.MailUtils;

@Component
public class SaveHiker {

	private HikerRepository hikerRepository;

	private Environment environment;

	@Autowired
	public SaveHiker(HikerRepository hikerRepository, Environment environment) {
		this.hikerRepository = hikerRepository;
		this.environment = environment;
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

		if (!MailUtils.assertPuedeEnviarEmails(this.environment)) {
			// En caso de estar en perfil de test, o prodPostgre
			// activamos el hiker sin correo electrónico
			h.setActive(Boolean.TRUE);
			hiker.setActive(Boolean.TRUE);
			throw new BusinessException(
					"No hemos podido enviarte un email. La cuenta ya está activada.");
		}

		if (!assertActiveHiker(hiker)) {
			try {
				String code = MailUtils.generateKey(h.getLogin());
				h.setActiveCode(code);
				MailUtils.sendEmail(h.getEmail(), "Confirmación de cuenta",
						"¡Bienvenido a OCAM!\n\nPara confirmar tu cuenta, "
								+ "por favor, sigue el siguiente enlace: https://victorcastanogutierrez.github.io/OCAM-web/#/access/"
								+ code);
			} catch (NoSuchAlgorithmException e) {
				throw new BusinessException("Error durante el registro.");
			}
		}
	}

	private boolean assertActiveHiker(HikerDTO hiker) {
		return hiker.getActive();
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
