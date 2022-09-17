package com.cafelog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.cafelog.handler.CafeLogAuthenticationSuccessHandler;
import com.cafelog.handler.LogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LogoutHandler logoutHandler;
    private final CafeLogAuthenticationSuccessHandler successHandler;

    @Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
				.antMatchers("/login").permitAll()
				.anyRequest().authenticated()
			)
			// .logout((logout) -> logout.permitAll())
            .oauth2Login()
                .loginPage("/login")
                .successHandler(successHandler)
                // .defaultSuccessUrl("/")
            .and()
            .logout()
                .logoutSuccessUrl("/login")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .addLogoutHandler(logoutHandler);
		http
			.csrf(csrf -> csrf
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			);
		return http.build();
	}

}