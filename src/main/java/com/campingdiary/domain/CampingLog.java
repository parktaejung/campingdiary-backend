package com.campingdiary.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CampingLog {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) // ← 여기가 오류인 경우
	private Long id;
	private LocalDate date;
	private String location;
	private Double latitude;
	private Double longitude;
	private String weather;
	
	@Column(columnDefinition = "TEXT")
	private String memo;
	
	private int rating;
	
	@ElementCollection
	private List<String> tags;
	
	@ElementCollection
	private List<String> photoUrls;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	@Column(nullable = false)
	private Boolean isPublic = true;
	@Column(nullable = false)
	private int likeCount = 0;
	@Column(nullable = false)
	private int viewCount = 0;
	@OneToMany(mappedBy = "campingLog", cascade= CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}
