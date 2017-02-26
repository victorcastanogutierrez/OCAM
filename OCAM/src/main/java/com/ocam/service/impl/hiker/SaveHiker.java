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

		String passwd = hiker.getPassword();
		try {
			hiker.setPassword(MD5Util.MD5(passwd));
		} catch (NoSuchAlgorithmException e) {
			throw new BusinessException(
					"Error en la encriptación de la contraseña del usuario "
							+ hiker.getLogin());
		}
		this.hikerRepository.save(hiker);
	}
}
