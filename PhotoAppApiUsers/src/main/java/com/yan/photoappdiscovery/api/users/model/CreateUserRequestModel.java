package com.yan.photoappdiscovery.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

	@NotNull(message = "First name is required")
	@Size(min = 2, message = "Must be longer than 2 characters")
	private String firstName;
	
	@NotNull(message = "Last name is required")
	@Size(min = 2, message = "Must be longer than 2 characters")
	private String lastName;
	
	@NotNull(message = "Email is required")
	@Email
	private String email;
	
	@NotNull(message = "Email is required")
	@Size(min = 8, max = 16)
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
