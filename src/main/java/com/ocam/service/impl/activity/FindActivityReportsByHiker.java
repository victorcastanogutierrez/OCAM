package com.ocam.service.impl.activity;

import java.util.Set;

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
public class FindActivityReportsByHiker {

	private ReportRepository reportRepository;
	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public FindActivityReportsByHiker(ReportRepository reportRepository,
			ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.reportRepository = reportRepository;
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Set<Report> execute(Long activityId, String hikerEmail) {

		if (!assertActivityId(activityId) || !assertHikerEmail(hikerEmail)) {
			return null;
		}

		Activity activity = activityRepository.findOne(activityId);
		Hiker hiker = hikerRepository.findByEmail(hikerEmail);

		if (assertActivityNotNull(activity) && assertHikerNotNull(hiker)) {
			return reportRepository.findAllByActivityAndHiker(activity, hiker);
		}
		return null;
	}

	private boolean assertActivityId(Long activityId) {
		return activityId != null;
	}

	private boolean assertHikerEmail(String login) {
		return login != null;
	}

	private Boolean assertHikerNotNull(Hiker hiker) {
		return hiker != null;
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
