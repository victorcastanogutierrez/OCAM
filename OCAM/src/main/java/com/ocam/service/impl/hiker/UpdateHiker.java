package com.ocam.service.impl.hiker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Hiker;
import com.ocam.repository.HikerRepository;

@Component
public class UpdateHiker {

	private HikerRepository hikerRepository;

	@Autowired
	public UpdateHiker(HikerRepository hikerRepository) {
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Hiker hiker) {
		this.hikerRepository.save(hiker);
	}
}
