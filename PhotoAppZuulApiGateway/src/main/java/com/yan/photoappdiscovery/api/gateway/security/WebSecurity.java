package com.yan.photoappdiscovery.api.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private @Autowired Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().disable()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, env.getProperty("api.login.url.path")).permitAll()
			.antMatchers(HttpMethod.POST, env.getProperty("api.register.url.path")).permitAll()
			.antMatchers("/users-ws/h2-console").permitAll()
			.antMatchers("/albums-ws/**").permitAll()
			.antMatchers(HttpMethod.GET, "/users-ws/users/ping").permitAll()
			.antMatchers(env.getProperty("api.zuul.actuator.url.path"), env.getProperty("api.users.actuator.url.path")).permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(authorizationFilter());
	}

	
	@Bean
	public AuthorizationFilter authorizationFilter() throws Exception {
		return new AuthorizationFilter(authenticationManager());
	}
	
	
}
