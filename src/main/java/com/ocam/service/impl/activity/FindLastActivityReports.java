package com.ocam.service.impl.activity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;
import com.ocam.model.exception.BusinessException;
import com.ocam.repository.ActivityRepository;
import com.ocam.repository.ReportRepository;

@Component
public class FindLastActivityReports {

	private ReportRepository reportRepository;
	private ActivityRepository activityRepository;

	@Autowired
	public FindLastActivityReports(ReportRepository reportRepository,
			ActivityRepository activityRepository) {
		this.reportRepository = reportRepository;
		this.activityRepository = activityRepository;
	}

	/**
	 * Obtiene la lista de reportes de una actividad ordenada decrecientemente
	 * por fechas. Esa lista la agrupa por Hiker y posteriormente se queda con
	 * el primer Report de cada Hiker (el que tiene, por tanto, fecha superior)
	 * 
	 * @param activityId
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public Set<Report> execute(Long activityId) throws BusinessException {
		if (!assertActivityId(activityId)) {
			throw new BusinessException("ID de actividad inválida");
		}

		Activity activity = activityRepository.findOne(activityId);
		if (!assertActivityNotNull(activity)) {
			throw new BusinessException("Error procesando la actividad");
		}

		Set<Report> reports = reportRepository
				.findAllByActivityOrderByDateDesc(activity);

		Map<Hiker, List<Report>> grouped = reports.stream()
				.collect(Collectors.groupingBy(Report::getHiker));

		Set<Report> result = new HashSet<Report>();
		for (Map.Entry<Hiker, List<Report>> entry : grouped.entrySet()) {
			result.add(entry.getValue().get(0));
		}

		return result.stream()
				.filter(x -> isInActivity(x.getHiker(), x.getActivity()))
				.collect(Collectors.toSet());
	}

	private Boolean assertActivityId(Long id) {
		return id != null;
	}

	/**
	 * Comprueba si un hiker está participando en una actividad
	 * 
	 * @param hiker
	 * @param activity
	 * @return
	 */
	private Boolean isInActivity(Hiker hiker, Activity activity) {
		for (Hiker h : activity.getHikers()) {
			if (h.getId().equals(hiker.getId())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private Boolean assertActivityNotNull(Activity activity) {
		return activity != null;
	}
}
