package com.ocam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Hiker;
import com.ocam.service.HikerService;
import com.ocam.service.impl.hiker.FindHikerByLoginPassword;
import com.ocam.service.impl.hiker.SaveHiker;
import com.ocam.service.impl.hiker.UpdateHiker;

@Service
public class HikerServiceImpl implements HikerService {

	private SaveHiker saveHiker;
	private FindHikerByLoginPassword findHikerByLoginPassword;
	private UpdateHiker updateHiker;

	@Autowired
	public HikerServiceImpl(FindHikerByLoginPassword findHikerByLoginPassword,
			SaveHiker saveHiker, UpdateHiker updateHiker) {
		this.findHikerByLoginPassword = findHikerByLoginPassword;
		this.saveHiker = saveHiker;
		this.updateHiker = updateHiker;
	}

	@Override
	public Hiker findHikerByLoginPassword(String login, String password) {
		return this.findHikerByLoginPassword.execute(login, password);
	}

	@Override
	public void updateHiker(Hiker hiker) {
		this.saveHiker.execute(hiker);
	}

	@Override
	public void saveHiker(Hiker hiker) {
		this.updateHiker.execute(hiker);
	}

}
