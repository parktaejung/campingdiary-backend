package com.campingdiary.dto;

import java.time.LocalDate;
import java.util.List;

import com.campingdiary.domain.CampingLog;
import com.campingdiary.domain.User;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampingLogRequestDto {
	@NotNull(message = "날짜는 필수입니다.")
	private LocalDate date;
	@NotBlank(message = "장소는 비워둘 수 없습니다")
	private String location;
	
	private Double latitude;
	private Double longitude;
	@NotBlank(message = "날씨를 입력해주세요")
	private String weather;
	@Size(max = 1000, message = "메모는 1000자 이하로 작성해주세요")
	private String memo;
	@Min(value = 1, message = "평점은 1 이상이어야 합니다")
	@Max(value = 5, message = "평점은 5 이하이어야 합니다")
	private int rating;
	
	private List<String> tags;
	private List<String> photoUrls;
	
	private Boolean isPublic;
	public CampingLog toEntity(User user) {
		CampingLog log = new CampingLog();
		log.setDate(this.date);
		log.setLocation(this.location);
		log.setLatitude(this.latitude);
		log.setLongitude(this.longitude);
		log.setWeather(this.weather);
		log.setMemo(this.memo);
		log.setRating(this.rating);
		log.setTags(this.tags);
		log.setPhotoUrls(this.photoUrls);
		log.setUser(user);
		log.setIsPublic(this.isPublic != null ? this.isPublic : false);
		log.setLikeCount(0);
		log.setViewCount(0);
		return log;
		
	}
}
