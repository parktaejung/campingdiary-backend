package com.campingdiary.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.campingdiary.domain.CampingLog;
import com.campingdiary.domain.User;
import com.campingdiary.dto.CampingLogRequestDto;
import com.campingdiary.dto.CampingLogResponseDto;
import com.campingdiary.dto.CampingLogUpdateRequestDto;
import com.campingdiary.service.CampingLogService;
import com.campingdiary.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/logs")
public class CampingLogController {
	private final CampingLogService campingLogService;
	private final UserService userService;
	public CampingLogController(CampingLogService campingLogService, UserService userService) {
		this.campingLogService = campingLogService;
		this.userService = userService;
	}
	
	@PostMapping
	public CampingLogResponseDto saveCampingLog(@Valid @RequestBody CampingLogRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
		User user = userService.getUserByUsername(userDetails.getUsername());
		CampingLog entity = request.toEntity(user);
		CampingLog saved = campingLogService.save(entity);
		return CampingLogResponseDto.from(saved);
	}
	
	@GetMapping("/location")
	public List<CampingLogResponseDto> getLogsByLocation(@RequestParam("location") String location){	
		return campingLogService.findByLocation(location).stream().map(CampingLogResponseDto::from).toList();
	}
	@GetMapping
	public List<CampingLogResponseDto> getAllLogs(){
		return campingLogService.findAll().stream().map(CampingLogResponseDto::from).toList();
	}
	@DeleteMapping("/{logId}")
	public ResponseEntity<Void> deleteCampingLog(@PathVariable("logId") Long id) {
		campingLogService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	@GetMapping("/{logId}")
	public CampingLogResponseDto getCampingLogById(@PathVariable("logId") Long id){
		CampingLog log = campingLogService.findById(id);
		return CampingLogResponseDto.from(log);
	}
	@PutMapping("/{logId}")
	public ResponseEntity<CampingLogResponseDto> updateLog(
		    @PathVariable Long logId,
		    @Valid @RequestBody CampingLogUpdateRequestDto dto,
		    @AuthenticationPrincipal UserDetails userDetails
		) {
		    User user = userService.getUserByUsername(userDetails.getUsername());
		    CampingLogResponseDto response = campingLogService.updateCampingLog(logId, dto, user);
		    return ResponseEntity.ok(response);
		}
	@GetMapping("/paged")
	public Page<CampingLog> getPagedLogs(@ParameterObject Pageable pageable){
		return campingLogService.findAll(pageable);
	}
}
