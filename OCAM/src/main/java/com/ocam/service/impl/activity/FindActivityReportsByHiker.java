package com.ocam.service.impl.activity;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.repository.ReportRepository;

@Component
public class FindActivityReportsByHiker {

	private ReportRepository reportRepository;

	@Autowired
	public FindActivityReportsByHiker(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	@Transactional(readOnly = true)
	public Set<Report> execute(Activity activity, Hiker hiker) {
		return reportRepository.findAllByActivityAndHiker(activity, hiker);
	}
}
