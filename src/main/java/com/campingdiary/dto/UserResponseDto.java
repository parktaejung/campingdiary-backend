package com.campingdiary.dto;

import java.time.LocalDate;

import com.campingdiary.domain.User;
import com.campingdiary.domain.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
	private Long id;
	private String username;
	private String nickname;
	private String profileImageUrl;
	private LocalDate createdAt;
	private Role role;
	
	public static UserResponseDto from(User user) {
		UserResponseDto dto = new UserResponseDto();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setNickname(user.getNickname());
		dto.setProfileImageUrl(user.getProfileImageUrl());
		dto.setCreatedAt(user.getCreatedAt());
		dto.setRole(user.getRole());
		return dto;
	}
}
