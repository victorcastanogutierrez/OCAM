package com.ocam.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.service.ReportService;
import com.ocam.service.impl.report.SaveReport;

@Service
public class ReportServiceImpl implements ReportService {

	private SaveReport saveReport;

	@Autowired
	public ReportServiceImpl(SaveReport saveReport) {
		this.saveReport = saveReport;
	}

	@Override
	public void save(Report report) throws BusinessException {
		this.saveReport.execute(report);
	}
}
