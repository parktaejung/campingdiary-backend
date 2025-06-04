package com.campingdiary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

	@NotBlank(message = "아이디는 필수입니다")
	private String username;
	
	@NotBlank(message = "패스워드는 필수입니다")
	private String password;
}
