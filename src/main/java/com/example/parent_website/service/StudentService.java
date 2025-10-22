package com.example.parent_website.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.parent_website.model.Student;
import com.example.parent_website.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    
    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUsername(username);
    }    
}