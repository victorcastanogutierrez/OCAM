package com.ocam.service.impl.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocam.model.Activity;
import com.ocam.model.ActivityDTO;
import com.ocam.model.exception.BusinessException;
import com.ocam.model.types.ActivityStatus;
import com.ocam.repository.ActivityRepository;

/**
 * Clase que busca actividades según criterios dados
 * 
 * @author Victor
 *
 */
@Component
public class FindAllPendingActivities {

	private ActivityRepository activityRepository;

	@Autowired
	public FindAllPendingActivities(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	/**
	 * Método que obtiene las actividades desde la fila min hasta la fila max.
	 * 
	 * Para ello crea un Pageable dividido en una página del tamaño máximo
	 * 
	 * Después se queda con los registros que le interesan (desde min hasta max)
	 * 
	 * 
	 * @param criteria
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(readOnly = true)
	public List<Activity> execute(ActivityDTO criteria)
			throws BusinessException {

		if (!assertValidCriteria(criteria)) {
			throw new BusinessException(
					"Datos de mínimo y máximo requeridos para la query");
		}

		Integer max = this.getMaxQueryValue(criteria);
		Integer min = this.getMinQueryValue(criteria);

		Pageable rows = new PageRequest(0, max);
		List<Activity> activities = activityRepository
				.findByStatusOrStatusOrderByStartDateDesc(
						ActivityStatus.RUNNING, ActivityStatus.PENDING, rows);

		if (min > activities.size()) {
			return new ArrayList<Activity>();
		} else {
			max = max > activities.size() ? activities.size() : max;
			return activities.subList(min, max);
		}
	}

	private Integer getMinQueryValue(ActivityDTO criteria)
			throws BusinessException {
		Integer min = 0;
		if (criteria.getMinResults() != null) {
			min = criteria.getMinResults();
		} else {
			throw new BusinessException(
					"Datos de mínimo y máximo requeridos para la query");
		}
		return min;
	}

	private Integer getMaxQueryValue(ActivityDTO criteria)
			throws BusinessException {
		Integer max = Integer.MAX_VALUE;
		if (criteria.getMaxResults() != null) {
			max = criteria.getMaxResults();
		} else {
			throw new BusinessException(
					"Datos de mínimo y máximo requeridos para la query");
		}
		return max;
	}

	private boolean assertValidCriteria(ActivityDTO criteria) {
		return criteria != null
				? criteria.getMaxResults() > criteria.getMinResults() : false;
	}
}
