package com.ocam.service.impl.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;

@Component
public class SaveActivityReport {

	private ActivityRepository activityRepository;
	private ReportRepository reportRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public SaveActivityReport(ActivityRepository activityRepository,
			ReportRepository reportRepository,
			HikerRepository hikerRepository) {
		this.activityRepository = activityRepository;
		this.reportRepository = reportRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Long activityId, Long hikerId, Report report) {
		Activity activity = activityRepository.findOne(activityId);
		Hiker hiker = hikerRepository.findOne(hikerId);
		if (assertActivityNotNull(activity) && assertHikerNotNull(hiker)) {
			report.setHiker(hiker);
			report.setActivity(activity);
			reportRepository.save(report);
			hiker.getReports().add(report);
			activity.getReports().add(report);
		}
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}

	private Boolean assertHikerNotNull(Hiker hiker) {
		return hiker != null;
	}
}
