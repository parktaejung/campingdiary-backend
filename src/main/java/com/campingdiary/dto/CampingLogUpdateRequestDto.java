package com.campingdiary.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampingLogUpdateRequestDto {

	@NotNull(message = "날짜는 필수입니다")
	private LocalDate date;
	
	@NotBlank(message = "장소는 필수입니다")
	private String location;
	private Double latitude;
	private Double longitude;
	
	@NotBlank(message = "날씨는 필수입니다")
	private String weather;
	
	private String memo;
	@Min(1)
	@Max(5)
	private int rating;
	
	private List<String> tags;
	private List<String> photoUrls;
	
	private Boolean isPublic;
}
