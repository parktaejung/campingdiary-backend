package com.campingdiary.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.campingdiary.domain.CampingLog;
import com.campingdiary.domain.User;
import com.campingdiary.dto.CampingLogResponseDto;
import com.campingdiary.dto.CampingLogUpdateRequestDto;
import com.campingdiary.repository.CampingLogRepository;

@Service
public class CampingLogService {
	private final CampingLogRepository campingLogRepository;
	
	public CampingLogService(CampingLogRepository campingLogRepsitory) {
		this.campingLogRepository = campingLogRepsitory;
	}
	
	public CampingLog save(CampingLog log) {
		return campingLogRepository.save(log);
	}
	public List<CampingLog> findAll(){
		return campingLogRepository.findAll();
	}
	public CampingLog findById(Long id) {
		return campingLogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 Id의 기록이 없습니다: "+id));
	}
	public List<CampingLog> findByLocation(String location){
		return campingLogRepository.findByLocation(location);
	}
	public void deleteById(Long id) {
		  try {
		        campingLogRepository.deleteById(id);
		    } catch (EmptyResultDataAccessException e) {
		        System.out.println("삭제하려는 ID가 존재하지 않습니다: " + id);
		        // 또는 무시하거나 로깅
		    }
	}
	public CampingLogResponseDto updateCampingLog(Long logId, CampingLogUpdateRequestDto requestDto, User user) {
		CampingLog log = campingLogRepository.findById(logId).orElseThrow(() -> new IllegalArgumentException("해당기록이 없습니다 :"+logId));
		
		if(!log.getUser().getId().equals(user.getId())) {
			throw new SecurityException("수정 권한이 없습니다");
		}
		log.setDate(requestDto.getDate());
		log.setLocation(requestDto.getLocation());
		log.setLatitude(requestDto.getLatitude());
		log.setLongitude(requestDto.getLongitude());
		log.setWeather(requestDto.getWeather());
		log.setMemo(requestDto.getMemo());
		log.setRating(requestDto.getRating());
		log.setTags(requestDto.getTags());
		log.setPhotoUrls(requestDto.getPhotoUrls());
		log.setIsPublic(requestDto.getIsPublic());
		
		 CampingLog updatedLog = campingLogRepository.save(log); 
		return CampingLogResponseDto.from(updatedLog);
	}
	public Page<CampingLog> findAll(Pageable pageable){
		return campingLogRepository.findAll(pageable);
	}
}
