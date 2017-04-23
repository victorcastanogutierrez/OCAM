package com.ocam.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.HikerDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.HikerService;
import com.ocam.service.impl.hiker.FindHikerByEmail;
import com.ocam.service.impl.hiker.FindHikerByLogin;
import com.ocam.service.impl.hiker.FindHikerByLoginPassword;
import com.ocam.service.impl.hiker.FindHikerFinishActivities;
import com.ocam.service.impl.hiker.SaveHiker;
import com.ocam.service.impl.hiker.UpdateHiker;
import com.ocam.service.impl.hiker.UpdateHikerPassword;
import com.ocam.service.impl.hiker.ValidateHiker;

@Service
public class HikerServiceImpl implements HikerService {

	private SaveHiker saveHiker;
	private FindHikerByLoginPassword findHikerByLoginPassword;
	private FindHikerByLogin findHikerByLogin;
	private UpdateHiker updateHiker;
	private UpdateHikerPassword updateHikerPassword;
	private FindHikerByEmail findHikerByEmail;
	private ValidateHiker validateHiker;
	private FindHikerFinishActivities findHikerFinishActivities;

	@Autowired
	public HikerServiceImpl(FindHikerByLoginPassword findHikerByLoginPassword,
			SaveHiker saveHiker, UpdateHiker updateHiker,
			FindHikerByLogin findHikerByLogin,
			UpdateHikerPassword updateHikerPassword,
			FindHikerByEmail findHikerByEmail, ValidateHiker validateHiker,
			FindHikerFinishActivities findHikerFinishActivities) {
		this.findHikerByLoginPassword = findHikerByLoginPassword;
		this.findHikerByLogin = findHikerByLogin;
		this.saveHiker = saveHiker;
		this.updateHiker = updateHiker;
		this.updateHikerPassword = updateHikerPassword;
		this.findHikerByEmail = findHikerByEmail;
		this.validateHiker = validateHiker;
		this.findHikerFinishActivities = findHikerFinishActivities;
	}

	@Override
	public Hiker findHikerByLoginPassword(String login, String password)
			throws BusinessException {
		return this.findHikerByLoginPassword.execute(login, password);
	}

	@Override
	public void updateHiker(Hiker hiker) throws BusinessException {
		this.updateHiker.execute(hiker);
	}

	@Override
	public void saveHiker(HikerDTO hiker) throws BusinessException {
		this.saveHiker.execute(hiker);
	}

	@Override
	public Hiker findHikerByLogin(String login) {
		return this.findHikerByLogin.execute(login);
	}

	@Override
	public void changePassword(HikerDTO hikerDto) throws BusinessException {
		this.updateHikerPassword.execute(hikerDto);
	}

	@Override
	public Hiker findHikerByEmail(String email) {
		return this.findHikerByEmail.execute(email);
	}

	@Override
	public void validateHiker(String code) throws BusinessException {
		this.validateHiker.execute(code);
	}

	@Override
	public List<Activity> findHikerFinishActivities(String login)
			throws BusinessException {
		return this.findHikerFinishActivities.execute(login);
	}
}
