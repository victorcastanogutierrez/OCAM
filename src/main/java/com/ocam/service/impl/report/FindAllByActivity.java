package com.ocam.service.impl.report;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.ReportRepository;

@Component
public class FindAllByActivity {

	private ReportRepository reportRepository;
	private ActivityRepository activityRepository;

	@Autowired
	public FindAllByActivity(ReportRepository reportRepository,
			ActivityRepository activityRepository) {
		this.reportRepository = reportRepository;
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = true)
	public Set<Report> execute(Long activityId) throws BusinessException {

		if (activityId == null) {
			throw new BusinessException(
					"Necesiaria actividad para buscar los reportes");
		}

		Activity activity = activityRepository.findOne(activityId);
		if (!assertActivityNotNull(activity)) {
			throw new BusinessException("Error procesando la actividad");
		}

		return reportRepository.findAllByActivityOrderByDateDesc(activity);
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
