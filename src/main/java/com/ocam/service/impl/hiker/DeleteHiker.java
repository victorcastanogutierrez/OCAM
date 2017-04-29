package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.HikerRepository;

@Component
public class DeleteHiker {

	private HikerRepository hikerRepository;

	@Autowired
	public DeleteHiker(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(String login) throws BusinessException {
		if (login == null) {
			throw new BusinessException("Hiker inválido");
		}

		Hiker h = hikerRepository.findByLogin(login);
		if (h == null) {
			throw new BusinessException("Hiker no existente");
		}

		h.setDeleted(Boolean.TRUE);
	}
}
