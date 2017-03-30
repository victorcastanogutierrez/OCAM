package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;

@Component
public class ValidateHiker {
	private HikerRepository hikerRepository;

	@Autowired
	public ValidateHiker(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(String code) throws BusinessException {

		if (!assertCode(code)) {
			throw new BusinessException("Código inválido");
		}

		Hiker hiker = hikerRepository.findTopByActiveCodeAndActive(code,
				Boolean.FALSE);

		if (!assertHiker(hiker)) {
			throw new BusinessException(
					"Código no asociado a ninguna cuenta de usuario");
		}

		hiker.setActive(Boolean.TRUE);
	}

	private boolean assertHiker(Hiker hiker) {
		return hiker != null;
	}

	private boolean assertCode(String code) {
		return code != null;
	}
}
