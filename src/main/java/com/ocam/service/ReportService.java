package com.ocam.service;

import java.util.Set;

import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;

public interface ReportService {

	void save(Report report) throws BusinessException;

	Set<Report> findHikerLastActivityReport(Long activityId, String login)
			throws BusinessException;

	Set<Report> findAllByActivity(Long activityId) throws BusinessException;
}
