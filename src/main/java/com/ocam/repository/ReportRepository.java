package com.ocam.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Activity;
import com.ocam.model.Hiker;
import com.ocam.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

	Set<Report> findAllByActivityOrderByDateDesc(Activity activity);

	Set<Report> findAllByActivityAndHiker(Activity activity, Hiker hiker);
}
