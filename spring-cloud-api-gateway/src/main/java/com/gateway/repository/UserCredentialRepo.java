package com.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gateway.entity.UserCredential;

@Repository
public interface UserCredentialRepo extends JpaRepository<UserCredential, Integer> {

	public UserCredential findByUserName(String username);

}
