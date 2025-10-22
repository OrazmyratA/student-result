package com.example.parent_website.service;

import com.example.parent_website.model.Student;
import com.example.parent_website.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsername(username);

        if (student.isPresent()) {
            Student foundStudent = student.get();
            
            return new User(
                    foundStudent.getUsername(),
                    foundStudent.getPassword(), // In a real application, this should be the *encoded* password
                    java.util.Collections.emptyList() // Replace with authorities/roles if needed
            );
        } else {
            throw new UsernameNotFoundException("Student not found with username: " + username);
        }
    }
}