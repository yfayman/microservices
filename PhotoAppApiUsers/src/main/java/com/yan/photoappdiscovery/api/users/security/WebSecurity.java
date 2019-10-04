package com.yan.photoappdiscovery.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.yan.photoappdiscovery.api.users.service.UsersService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private @Autowired Environment env;
	
	
	private @Autowired UsersService userSevice;
	private @Autowired BCryptPasswordEncoder encoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and()
			.authorizeRequests().anyRequest().permitAll()
				//.antMatchers("/**").hasIpAddress(env.getProperty("gateway.ip"))
			.and()
				.csrf()
				.disable()
				.addFilter(authenticationFilter());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userSevice).passwordEncoder(encoder);
	}
	
	
	@Bean
	public AuthenticationFilter authenticationFilter() throws Exception {
		AuthenticationFilter filter = new AuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setFilterProcessesUrl(env.getProperty("login.url.path"));
		return filter;
	}
	
	
	
	
}
