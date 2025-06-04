package com.campingdiary.dto;

import java.time.LocalDate;
import java.util.List;

import com.campingdiary.domain.CampingLog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CampingLogResponseDto {
 	private Long id;
    private LocalDate date;
    private String location;
    private Double latitude;
    private Double longitude;
    private String weather;
    private String memo;
    private int rating;
    private List<String> tags;
    private List<String> photoUrls;
    private String nickname;
    private Boolean isPublic;
    private int likeCount;
    private int viewCount;
    
    public static CampingLogResponseDto from(CampingLog entity) {
    	return CampingLogResponseDto.builder()
    			.id(entity.getId())
    			.date(entity.getDate())
    			.location(entity.getLocation())
    			.latitude(entity.getLatitude())
    			.longitude(entity.getLongitude())
    			.weather(entity.getWeather())
    			.memo(entity.getMemo())
    			.rating(entity.getRating())
    			.tags(entity.getTags())
    			.photoUrls(entity.getPhotoUrls())
    			.nickname(entity.getUser().getNickname())
    			.isPublic(entity.getIsPublic())
    			.likeCount(entity.getLikeCount())
    			.viewCount(entity.getViewCount())
    			.build();
    }
}
