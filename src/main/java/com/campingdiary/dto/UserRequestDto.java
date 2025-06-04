package com.campingdiary.dto;

import com.campingdiary.domain.User;
import com.campingdiary.domain.enums.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
	@NotBlank(message = "아이디는 필수입니다")
	private String username;
	
	@NotBlank(message = "닉네임은 필수입니다")
	private String nickname;
	
	@NotBlank(message = "비밀번호는 필수입니다")
	private String password;
	
	
	
	public User toEntity() {
		User user = new User();
		user.setUsername(this.username);
		user.setNickname(this.nickname);
		user.setPassword(this.password);
		user.setRole(Role.USER);
		return user;
	}
}
