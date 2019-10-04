package com.yan.photoappdiscovery.api.users.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bouncycastle.crypto.tls.UseSRTPData;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yan.photoappdiscovery.api.users.data.AlbumsServiceClient;
import com.yan.photoappdiscovery.api.users.data.UserEntity;
import com.yan.photoappdiscovery.api.users.data.UsersRepository;
import com.yan.photoappdiscovery.api.users.model.AlbumResponseModel;
import com.yan.photoappdiscovery.api.users.model.UserDto;

@Service
public class UsersServiceImpl implements UsersService {
	
	private static Logger logger =LoggerFactory.getLogger(UsersServiceImpl.class);

	private UsersRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private @Autowired Environment env;
	private @Autowired AlbumsServiceClient albumClient;
	
	@Autowired
	public UsersServiceImpl(UsersRepository repository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@Override
	public UserDto createUser(UserDto userDetails) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));
		
		UserEntity user = mapper.map(userDetails, UserEntity.class);
		
		UserDto userDto = mapper.map(userRepository.save(user), UserDto.class);
		return userDto;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(username);
		if(user == null) { throw new UsernameNotFoundException("Oh no"); }
		
		return new User(user.getEmail() , user.getEncryptedPassword(), Collections.emptyList());
	}


	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity user = userRepository.findByEmail(email);
		
		if(user == null) { throw new UsernameNotFoundException(email + " not found"); }
		
		return new ModelMapper().map(user, UserDto.class);
	}


	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity user = userRepository.findByUserId(userId);
		if(user == null) { throw new UsernameNotFoundException(userId + " not found"); }
		
		UserDto dto = new ModelMapper().map(user, UserDto.class);

		dto.setAlbums(albumClient.getAlbums(userId));
		
		return dto;
	}

}
