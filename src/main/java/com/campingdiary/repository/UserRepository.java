package com.campingdiary.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campingdiary.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	Optional<User> findByUsername(String username);
	Optional<User> findByNickname(String nickname);
	boolean existsByNickname(String nickname);
	boolean existsByUsername(String username);
}
