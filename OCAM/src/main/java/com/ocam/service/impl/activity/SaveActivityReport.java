package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Report;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.ReportRepository;

@Component
public class SaveActivityReport {

	private ActivityRepository activityRepository;
	private ReportRepository reportRepository;

	@Autowired
	public SaveActivityReport(ActivityRepository activityRepository,
			ReportRepository reportRepository) {
		this.activityRepository = activityRepository;
		this.reportRepository = reportRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, Report report) {
		Activity activity = activityRepository.findOne(activityId);
		if (assertActivityNotNull(activity)) {
			report.setActivity(activity);
			reportRepository.save(report);
			activity.getReports().add(report);
		}
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
