package com.ocam.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocam.model.Activity;
import com.ocam.model.types.ActivityStatus;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	List<Activity> findByStatusOrStatusAndDeletedOrderByStartDateDesc(
			ActivityStatus status, ActivityStatus status1, Boolean deleted,
			Pageable pageable);

	@Query("select a.status from Activity a where a.id = :id")
	ActivityStatus findActivityStatusById(@Param("id") Long id);

}
