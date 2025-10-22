package com.example.parent_website.controller;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; // Import for key generation
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.parent_website.model.Student;
import com.example.parent_website.repository.StudentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.crypto.SecretKey;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/login")
public class ParentController {

    private final AuthenticationManager authenticationManager;
    private final StudentRepository studentRepository;

    @Value(value="${jwt.secret}") // Inject the JWT secret from application.properties
    private String jwtSecret;

    public ParentController(AuthenticationManager authenticationManager, StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // Create an authentication request
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // If authentication is successful:
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<Student> studentOptional = studentRepository.findByUsername(username);
            if (studentOptional.isPresent()) {
                String token = generateJwtToken(username); // Call a method for JWT generation
                System.out.println(username);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("token", token);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                return new ResponseEntity<>(response, headers, HttpStatus.OK);
                //return ResponseEntity.ok(response, headers);
                
            }
        } catch (AuthenticationException e) {
            // Authentication failed
            // Log the error (for debugging)
            System.err.println("Authentication failed: " + e.getMessage()); // Or use a logger
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid username or password"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid username or password"));
    }

    private String generateJwtToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}