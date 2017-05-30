package com.ocam.service.impl.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
public class SaveReport {

	private HikerRepository hikerRepository;
	private ReportRepository reportRepository;
	private ActivityRepository activityRepository;

	@Autowired
	public SaveReport(HikerRepository hikerRepository,
			ReportRepository reportRepository,
			ActivityRepository activityRepository) {
		this.hikerRepository = hikerRepository;
		this.reportRepository = reportRepository;
		this.activityRepository = activityRepository;
	}

	@Transactional(readOnly = false)
	public void execute(Report report) throws BusinessException {

		assertReportData(report);

		Hiker h = hikerRepository.findByLogin(report.getHiker().getLogin());
		if (!assertHiker(h)) {
			throw new BusinessException(
					"Hiker asociado al report no encontrado");
		}

		Activity act = activityRepository.findActivityRunningByHiker(h,
				new PageRequest(0, 1));
		if (!assertActivity(act)) {
			throw new BusinessException(
					"El hiker no participa en ninguna actividad");
		}

		if (!assertHikerParticipante(act, h)) {
			throw new BusinessException(
					"El hiker no está registrado como participante de la actividad");
		}

		if (!assertGPSPoint(report)) {
			throw new BusinessException(
					"Datos de posicionamiento del hiker inválidos.");
		}

		Report newReport = new Report();
		newReport.setActivity(act);
		newReport.setHiker(h);
		newReport.setDate(report.getDate());
		newReport.setPoint(report.getPoint());
		reportRepository.save(newReport);

		act.getReports().add(newReport);
		h.getReports().add(newReport);
	}

	/**
	 * Busca la actividad en la que el Hiker está como participante
	 * 
	 * @param h
	 * @return
	 */
	/*
	 * private Activity findActivityHiker(Hiker h) { return
	 * h.getActivities().stream() .filter(x ->
	 * ActivityStatus.RUNNING.equals(x.getStatus()) &&
	 * Boolean.FALSE.equals(x.getDeleted())) .findFirst().orElse(null); }
	 */

	/**
	 * Comprueba que el hiker pertenezca como participante a la actividad
	 * 
	 * @param activity
	 * @param hiker
	 * @return
	 */
	private Boolean assertHikerParticipante(Activity activity, Hiker hiker) {
		for (Hiker h : activity.getHikers()) {
			if (h.getId().equals(hiker.getId())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Comprueba que lleguen los datos correctamente al servicio
	 * 
	 * @param report
	 * @throws BusinessException
	 */
	private void assertReportData(Report report) throws BusinessException {
		if (report == null) {
			throw new BusinessException("Datos de reporte inválidos");
		}

		if (report.getHiker() == null || report.getHiker().getLogin() == null) {
			throw new BusinessException(
					"Datos de hiker asociado al reporte inválidos");
		}
	}

	private boolean assertHiker(Hiker h) {
		return h != null;
	}

	private boolean assertActivity(Activity act) {
		return act != null;
	}

	private boolean assertGPSPoint(Report report) {
		return report.getPoint() != null;
	}
}
