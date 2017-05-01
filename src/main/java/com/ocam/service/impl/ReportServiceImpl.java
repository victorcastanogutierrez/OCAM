package com.ocam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.ReportService;
import com.ocam.service.impl.report.FindAllByActivity;
import com.ocam.service.impl.report.FindLastHikerActivityReport;
import com.ocam.service.impl.report.SaveReport;

@Service
public class ReportServiceImpl implements ReportService {

	private SaveReport saveReport;
	private FindLastHikerActivityReport findLastHikerActivityReport;
	private FindAllByActivity findAllByActivity;

	@Autowired
	public ReportServiceImpl(SaveReport saveReport,
			FindLastHikerActivityReport findLastHikerActivityReport,
			FindAllByActivity findAllByActivity) {
		this.saveReport = saveReport;
		this.findLastHikerActivityReport = findLastHikerActivityReport;
		this.findAllByActivity = findAllByActivity;
	}

	@Override
	public void save(Report report) throws BusinessException {
		this.saveReport.execute(report);
	}

	@Override
	public Set<Report> findHikerLastActivityReport(Long activityId,
			String login) throws BusinessException {
		return this.findLastHikerActivityReport.execute(activityId, login);
	}

	@Override
	public Set<Report> findAllByActivity(Long activityId)
			throws BusinessException {
		return this.findAllByActivity.execute(activityId);
	}
}
