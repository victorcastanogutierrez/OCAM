package com.ocam.service;

import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;

public interface ReportService {

	void save(Report report) throws BusinessException;

}
