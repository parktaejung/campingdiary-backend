package com.campingdiary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campingdiary.domain.CampingLog;

@Repository
public interface CampingLogRepository extends JpaRepository<CampingLog, Long>{
	// 기본적인 CRUD는 JpaRepository가 제공해줌
    // 나중에 필요한 경우: findByDate(LocalDate date) 같은 쿼리 추가 가능
	List<CampingLog> findByLocation(String location);
}
