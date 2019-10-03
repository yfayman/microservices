package com.yan.photoappdiscovery.api.users.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yan.photoappdiscovery.api.users.model.CreateUserRequestModel;
import com.yan.photoappdiscovery.api.users.model.CreateUserResponseModel;
import com.yan.photoappdiscovery.api.users.model.UserDto;
import com.yan.photoappdiscovery.api.users.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {

	private @Autowired Environment env;
	
	private @Autowired UsersService usersService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port") + 
				" using secret " + env.getProperty("token.secret");
	}
	
	@PostMapping(
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}			
	)
	public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody @Valid CreateUserRequestModel userDetails) {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto dto = mapper.map(userDetails, UserDto.class);
		UserDto createdUser = usersService.createUser(dto);
		
		CreateUserResponseModel response = mapper.map(createdUser, CreateUserResponseModel.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
