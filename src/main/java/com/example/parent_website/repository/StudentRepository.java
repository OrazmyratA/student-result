package com.example.parent_website.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parent_website.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

	Optional<Student> findByUsername(String username); // Add this method

	List<Student> findByClassName(String className);

}
