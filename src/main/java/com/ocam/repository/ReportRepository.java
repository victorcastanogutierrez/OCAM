package com.ocam.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

	Set<Report> findAllByActivityOrderByDateDesc(Activity activity);

	Set<Report> findAllByActivityAndHiker(Activity activity, Hiker hiker);

	@Query("select r from Report r join r.activity a where r.hiker.login = :login and a.id = :activityId order by r.date desc")
	List<Report> findByHikerActivity(@Param("activityId") Long activityId,
			@Param("login") String login, Pageable pageable);
}
