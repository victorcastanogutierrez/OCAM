package com.ocam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
