package com.yan.photoappdiscovery.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.yan.photoappdiscovery.api.users.model.UserDto;

public interface UsersService extends UserDetailsService{

	public UserDto createUser(UserDto userDetails);
	
	public UserDto getUserDetailsByEmail(String email);
}
