package com.ocam.service;

import com.ocam.model.Hiker;
import com.ocam.model.exception.BusinessException;

public interface HikerService {

	Hiker findHikerByLoginPassword(String login, String password)
			throws BusinessException;

	Hiker findHikerByLogin(String login);

	void updateHiker(Hiker hiker) throws BusinessException;

	void saveHiker(Hiker hiker) throws BusinessException;

}
