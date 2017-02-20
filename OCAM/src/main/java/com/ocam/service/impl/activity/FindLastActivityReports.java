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
import com.ocam.repository.ReportRepository;

@Component
public class FindLastActivityReports {

	private ReportRepository reportRepository;

	@Autowired
	public FindLastActivityReports(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	/**
	 * Obtiene la lista de reportes de una actividad ordenada decrecientemente
	 * por fechas. Esa lista la agrupa por Hiker y posteriormente se queda con
	 * el primer Report de cada Hiker (el que tiene, por tanto, fecha superior)
	 * 
	 * @param activity
	 *            actividad de la cual se buscan los reportes
	 * @return
	 */
	@Transactional(readOnly = true)
	public Set<Report> execute(Activity activity) {
		Set<Report> reports = reportRepository
				.findAllByActivityOrderByDateDesc(activity);

		Map<Hiker, List<Report>> grouped = reports.stream()
				.collect(Collectors.groupingBy(Report::getHiker));

		Set<Report> result = new HashSet<Report>();
		for (Map.Entry<Hiker, List<Report>> entry : grouped.entrySet()) {
			result.add(entry.getValue().get(0));
		}

		return result;
	}
}
