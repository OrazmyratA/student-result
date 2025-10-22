package com.example.parent_website.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.parent_website.repository.StudentRepository;
import com.example.parent_website.model.Student;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// 1. Configure CORS using the Bean below
				.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))

				// Disable CSRF protection (common for stateless APIs, but be aware of
				// production implications)
				.csrf(AbstractHttpConfigurer::disable)

				// 2. Authorize requests
				.authorizeHttpRequests(authz -> authz
						// *** CRUCIAL FOR CORS PREFLIGHT ***
						// Permit OPTIONS requests globally. This allows the browser's preflight request
						// to pass.
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						// Permit access to your login endpoint.
						// IMPORTANT: ENSURE THIS PATH EXACTLY MATCHES YOUR CONTROLLER'S MAPPING!
						// If ParentController has @RequestMapping("/login") and method @PostMapping,
						// it's "/login".
						// If ParentController has @RequestMapping("/api") and method
						// @PostMapping("/login"), it's "/api/login".
						.requestMatchers(new AntPathRequestMatcher("/login")).permitAll().requestMatchers("/")
						.permitAll()
						// Authenticate other protected endpoints
						.requestMatchers(new AntPathRequestMatcher("/api/students/profile")).authenticated()
						.anyRequest().authenticated())
				// Add your JWT filter BEFORE the default UsernamePasswordAuthenticationFilter
				// Ensure jwtAuthenticationFilter is correctly defined and injected
				// *** Add this ***
			
				 // .requiresChannel(channel -> channel.anyRequest().requiresSecure() )
				  
				 
				// 4. JWT Filter
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// *** Correct way to set multiple origins ***
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4202", "http://localhost:4202", "http://127.0.0.1:4040",
				"http://192.168.1.101:4202", "http://192.168.1.102:4202", "http://192.168.1.100:4202",
				"http://192.168.137.1:4202", "http://192.168.205.87:4202", "http://192.168.203.87:4202",
				"http://192.168.80.87:4202", "http://10.160.87.211:4202", "http://10.112.239.87:4202",
				"http://10.10.20.35:4202", "http://10.211.1.12::4202", "http://8928ca34ff6f.ngrok-free.app",
				"https://dbff77b514e9.ngrok-free.app"));

		// *** Consolidate allowed methods and headers ***
		// Explicitly include OPTIONS method.
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Added OPTIONS
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept")); // Specific headers

		// Set allowCredentials last for clarity
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Apply to all paths
		return source;
	}

}