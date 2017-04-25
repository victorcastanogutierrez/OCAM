package com.ocam.service.impl.report;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.HikerRepository;
import com.ocam.repository.ReportRepository;

@Component
public class FindLastHikerActivityReport {

	private ReportRepository reportRepository;
	private ActivityRepository activityRepository;
	private HikerRepository hikerRepository;

	@Autowired
	public FindLastHikerActivityReport(ReportRepository reportRepository,
			ActivityRepository activityRepository,
			HikerRepository hikerRepository) {
		this.reportRepository = reportRepository;
		this.activityRepository = activityRepository;
		this.hikerRepository = hikerRepository;
	}

	@Transactional(readOnly = true)
	public Set<Report> execute(Long activityId, String login)
			throws BusinessException {
		Activity activity = activityRepository.findOne(activityId);
		if (!assertActivityNotNull(activity)) {
			throw new BusinessException("Error procesando la actividad");
		}

		if (!assertEmail(login)) {
			throw new BusinessException("Login inválido");
		}

		Hiker h = this.hikerRepository.findByLogin(login);
		if (!assertHikerNotNull(h)) {
			throw new BusinessException("Hiker no encontrado");
		}
		Pageable rows = new PageRequest(0, 1);
		return new HashSet<Report>(
				reportRepository.findByHikerActivity(activityId, login, rows));
	}

	private boolean assertHikerNotNull(Hiker h) {
		return h != null;
	}

	private boolean assertEmail(String email) {
		return email != null;
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
