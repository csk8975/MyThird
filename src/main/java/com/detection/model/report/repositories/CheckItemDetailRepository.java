package com.detection.model.report.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.detection.model.report.entities.CrCheckItemDetail;

public interface CheckItemDetailRepository extends JpaRepository<CrCheckItemDetail, Integer> {

}
