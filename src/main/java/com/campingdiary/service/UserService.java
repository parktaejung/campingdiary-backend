package com.campingdiary.service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.campingdiary.domain.User;
import com.campingdiary.dto.LoginRequestDto;
import com.campingdiary.dto.LoginResponseDto;
import com.campingdiary.dto.TokenRequestDto;
import com.campingdiary.dto.UserRequestDto;
import com.campingdiary.dto.UserResponseDto;
import com.campingdiary.dto.UserUpdateRequestDto;
import com.campingdiary.repository.UserRepository;
import com.campingdiary.security.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	
	@Transactional
	public UserResponseDto register(UserRequestDto requestDto) {
		if(userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 아이디입니다");
		}
		
		if(userRepository.findByNickname(requestDto.getNickname()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
		}
		
		User user = requestDto.toEntity();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = userRepository.save(user);
		
		
		return UserResponseDto.from(savedUser);
		
	}
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole().name()) // ex) "USER"
            .build();
    }
	
	@Transactional
	public LoginResponseDto login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("아이디가 존재하지않습니다"));
		
		if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
		}
		String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole().name());
		String refreshToken = jwtUtil.createRefreshToken(user.getUsername(), user.getRole().name()); // 새로 생성
		
		user.setRefreshToken(refreshToken);
		userRepository.save(user);
		return new LoginResponseDto(accessToken,refreshToken,user.getUsername(),user.getNickname());
	}
	
	@Transactional
	public LoginResponseDto reissue(TokenRequestDto tokenRequestDto) {
		String refreshToken = tokenRequestDto.getRefreshToken();
		if(!jwtUtil.validateToken(refreshToken)) {
			throw new IllegalArgumentException("RefreshToken이 유효하지않습니다");
		}
		String username = jwtUtil.getUsernameFromToken(refreshToken);
		User user =  userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		if(!refreshToken.equals(user.getRefreshToken())) {
			throw new IllegalArgumentException("RefreshToken 불일치");
		}
		String newAccessToken = jwtUtil.createToken(user.getUsername(), user.getRole().name());
		return new LoginResponseDto(newAccessToken,refreshToken,user.getUsername(),user.getNickname());
	}
	@Transactional
	public void logout(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
		user.setRefreshToken(null);
		userRepository.save(user);
	}
	@Transactional
	public UserResponseDto getUserInfo(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
		return UserResponseDto.from(user);
	}
	@Transactional
	public UserResponseDto updateUserInfo(String username, UserUpdateRequestDto requestDto) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
		if(!user.getNickname().equals(requestDto.getNickname()) && userRepository.existsByNickname(requestDto.getNickname())) {
			throw new IllegalArgumentException("이미 존재하는 닉네임 입니다");
		}
		
		user.setNickname(requestDto.getNickname());
		if(requestDto.getNewPassword() != null && !requestDto.getNewPassword().isBlank()) {
			if(requestDto.getCurrentPassword() == null || !passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())){
				throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다");
			}
			user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
		}
		return UserResponseDto.from(userRepository.save(user));
	}
	public User getUserByUsername(String username) {
	    return userRepository.findByUsername(username)
	        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
	}
}
