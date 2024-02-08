package com.springboot.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.springboot.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SpringSecurityconfig {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						(authorize) -> authorize.requestMatchers("/user/**").authenticated().anyRequest().permitAll())
				.formLogin(form -> form.loginPage("/signin").defaultSuccessUrl("/users/profile").failureHandler((request, response, exception) -> {
				    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    request.getSession().setAttribute("username", username);
                    request.getSession().setAttribute("password", password);
                    request.getSession().setAttribute("errorMsg", "Invalid username or password.");
                    response.sendRedirect("/signin?error");
                }));

		return http.build();
	}

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public DaoAuthenticationProvider authprovider() {
		DaoAuthenticationProvider dp = new DaoAuthenticationProvider();
		dp.setUserDetailsService(getUserDetailsService());

		dp.setPasswordEncoder(passwordEncoder());
		return dp;

	}

}

//.and().logout().logoutUrl("/perform_logout")
// .logoutSuccessUrl("/login?message=logout")