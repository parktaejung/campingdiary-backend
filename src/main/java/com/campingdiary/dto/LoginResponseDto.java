package com.campingdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
	private String accesstoken;
	private String refreshtoken;
	private String username;
	private String nickname;
	
}
