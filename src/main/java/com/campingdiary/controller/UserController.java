package com.campingdiary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campingdiary.dto.LoginRequestDto;
import com.campingdiary.dto.LoginResponseDto;
import com.campingdiary.dto.TokenRequestDto;
import com.campingdiary.dto.UserRequestDto;
import com.campingdiary.dto.UserResponseDto;
import com.campingdiary.dto.UserUpdateRequestDto;
import com.campingdiary.security.JwtUtil;
import com.campingdiary.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;
	public UserController(UserService userService,JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto requestDto){
		UserResponseDto responseDto = userService.register(requestDto);
		return ResponseEntity.ok(responseDto);
	}
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto){
		LoginResponseDto responseDto = userService.login(requestDto);
		return ResponseEntity.ok(responseDto);
	}
	@PostMapping("/token")
	public ResponseEntity<LoginResponseDto> reissue(@Valid @RequestBody TokenRequestDto requestDto){
		return ResponseEntity.ok(userService.reissue(requestDto));
	}
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request){
		String token = jwtUtil.resolveToken(request);
		if(token != null && jwtUtil.validateToken(token)) {
			String username = jwtUtil.getUsernameFromToken(token);
			userService.logout(username);
			return ResponseEntity.ok("로그아웃 완료");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다");
		}
	}
	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getUserInfo(HttpServletRequest request){
		String token = jwtUtil.resolveToken(request);
		if(token == null || !jwtUtil.validateToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String username = jwtUtil.getUsernameFromToken(token);
		UserResponseDto userResponseDto = userService.getUserInfo(username);
		return ResponseEntity.ok(userResponseDto);
	}
	@PostMapping("/update")
	public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails){
		String username = userDetails.getUsername();
		UserResponseDto responseDto = userService.updateUserInfo(username, requestDto);
		return ResponseEntity.ok(responseDto);
	}
}
