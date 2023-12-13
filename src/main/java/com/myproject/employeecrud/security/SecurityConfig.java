package com.myproject.employeecrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// add @configuration annotation
@Configuration
public class SecurityConfig {
	
	// define bean having method for providing
	// implicit user with pwds acc to roles
	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		
		UserDetails john = User.builder()
							.username("john")
							.password("{noop}john@01")
							.roles("EMPLOYEE")
							.build();
		
		UserDetails mary = User.builder()
				.username("mary")
				.password("{noop}mary@02")
				.roles("EMPLOYEE", "MANAGER")
				.build();
		
		UserDetails susan = User.builder()
				.username("susan")
				.password("{noop}susan@03")
				.roles("EMPLOYEE", "MANAGER", "ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(john, mary, susan);
		
	}
	
	// define bean with securityfilterchain for
	// restricting access based on roles
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// authorize request acc to roles
		http.authorizeHttpRequests(configurer ->
											configurer
											.requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
											.requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")
											.requestMatchers(HttpMethod.POST, "/api/employees").hasRole("MANAGER")
											.requestMatchers(HttpMethod.PUT, "/api/employees").hasRole("MANAGER")
											.requestMatchers(HttpMethod.DELETE, "/api/employees").hasRole("ADMIN")
				);
		
		// use http basic authentication
		http.httpBasic(Customizer.withDefaults());
		
		// disable cross site request forgery csrf
		// not required for stateless REST Api's that use POST, PUT, DELETE and/or PATCH
		http.csrf(csrf -> csrf.disable());
		
		// return http.build()
		return http.build();
		
	}

}













