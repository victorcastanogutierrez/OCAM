package com.ocam.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ocam.model.Activity;
import com.ocam.model.types.ActivityStatus;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

	Set<Activity> findAllByStatus(ActivityStatus status);

}
