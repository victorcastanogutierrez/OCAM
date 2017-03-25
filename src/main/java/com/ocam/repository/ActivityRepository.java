package com.ocam.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Activity;
import com.ocam.model.types.ActivityStatus;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByStatusOrStatusAndDeletedOrderByStartDateDesc(
			ActivityStatus status, ActivityStatus status1, Boolean deleted,
			Pageable pageable);

}
