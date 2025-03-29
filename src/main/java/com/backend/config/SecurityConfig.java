package com.backend.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final String[] PUBLIC_ENDPOINTS = {
			"/api/**", 
			"/home",
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
						//.requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
						.anyRequest().authenticated()
		);
		
		httpSecurity
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtCongfig -> jwtCongfig
						.decoder(jwtDecoder())
						//.jwtAuthenticationConverter(jwtAuthenticationConverter())
				)
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
		);
		
		return httpSecurity.build();
	}

	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // bo prefix SCOPE_
		//jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // neu muon lay authorities tu claim roles

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}

	@Bean
	JwtDecoder jwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(
				"oumNLE7DVaVyEYUlV9TSa/Hp7uMDGB6qrXlyaQPzCapHNO1rL9y2xkM8G3NhjrHI".getBytes(),
				"HS512");
		return NimbusJwtDecoder
				.withSecretKey(secretKeySpec)
				.macAlgorithm(MacAlgorithm.HS512)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}