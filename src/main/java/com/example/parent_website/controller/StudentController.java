package com.example.parent_website.controller;

import com.example.parent_website.dto.StudentProfileDto;
import com.example.parent_website.dto.LevelDto;
import com.example.parent_website.model.Student;
import com.example.parent_website.repository.StudentRepository;
import com.example.parent_website.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import for logging
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j // Add this for logging
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    @GetMapping("/profile")
    public ResponseEntity<StudentProfileDto> getStudentProfile() {
        try {
            // 1. Get the username of the logged-in user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // 2. Get Student from Database by Username
            Student student = studentService.getStudentByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Student not found with username: " + username));

            // 4. Prepare the Response DTO
            StudentProfileDto profileData = new StudentProfileDto();
            profileData.setUsername(student.getUsername());
            profileData.setFirstName(student.getFirstName());
            profileData.setLastName(student.getLastName());
            profileData.setScore(student.getScore());
            profileData.setPoint(student.getPoint());
            profileData.setRankingPercentage(calculateRankingPercentage(student));
            profileData.setLevels(buildLevelsData(student)); // Use the LevelDto here
            profileData.setSuccess(true);
            System.out.println(profileData);
            return ResponseEntity.ok(profileData);
        } catch (UsernameNotFoundException e) {
            // Handle the exception (log, return error response, etc.)
            log.warn("Student not found: {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(false,e.getMessage())); // Return the DTO with the error
        } catch (Exception e) {
            // Handle other exceptions
            log.warn("Error retrieving profile: {}", e.getMessage(), e); // Log the error, including the stack trace

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorResponse(false,"An error occurred while retrieving the profile.")); // Return the DTO with the error
        }
    }

    private double calculateRankingPercentage(Student student) {
        if (student == null || student.getClassName() == null || student.getScore() == null) {
            log.warn("Student, className, or score is null, returning default ranking percentage 0.0");
            return 0.0; // Or handle the null case differently
        }

        String className = student.getClassName();
        Integer score = student.getScore();

        // Get all students from the same class
        List<Student> studentsInClass = studentRepository.findByClassName(className);

        if (studentsInClass.isEmpty()) {
            log.info("No students found in class {}, returning default ranking percentage 0.0", className);
            return 0.0; // Or handle the case where there are no students in the class
        }

        // Find the minimum and maximum scores in the class
        Integer minScore = studentsInClass.stream()
                .filter(s -> s.getScore() != null)
                .map(Student::getScore)
                .min(Integer::compareTo)
                .orElse(0); // Handle the case where there are no scores

        Integer maxScore = studentsInClass.stream()
                .filter(s -> s.getScore() != null)
                .map(Student::getScore)
                .max(Integer::compareTo)
                .orElse(0); // Handle the case where there are no scores

        // Handle the case where all students have the same score
        if (minScore.equals(maxScore)) {
            log.info("All students in class {} have the same score, returning 100.0", className);
            return 100.0; // Return 100% to indicate top ranking
        }

        // Calculate the ranking percentage
        double rankingPercentage = (double) (score - minScore) / (maxScore - minScore) * 100;

        // Ensure percentage is within the valid range [0, 100]
        rankingPercentage = Math.max(0, Math.min(100, rankingPercentage));

        return rankingPercentage;
    }
    private List<LevelDto> buildLevelsData(Student student) {
        List<LevelDto> levelsData = new ArrayList<>();

        addLevelData(levelsData, "Let's go", student.getLetsGo());
        addLevelData(levelsData, "Beginner", student.getBeginner());
        addLevelData(levelsData, "Elementary", student.getElementary());
        addLevelData(levelsData, "Pre-Intermediate", student.getPreIntermediate());
        addLevelData(levelsData, "Intermediate", student.getIntermediate());
        addLevelData(levelsData, "Upper-Intermediate", student.getUpperIntermediate());
        addLevelData(levelsData, "Advance", student.getAdvance());

        return levelsData;
    }

    private void addLevelData(List<LevelDto> levelsData, String levelName, String levelValue) {
        LevelDto level = new LevelDto(); // Create the DTO
        level.setName(levelName);

        if (levelValue != null) {
            if (levelValue.contains("%")) {
                String remainingDaysStr = levelValue.replace("%", "");
                try {
                    int remainingDays = Integer.parseInt(remainingDaysStr);
                    level.setScore(null);
                    level.setCompleted(false);
                    level.setRemainingDays(remainingDays);
                } catch (NumberFormatException e) {
                    log.warn("Invalid remaining days format for level {}: {}", levelName, levelValue);
                    level.setScore(null);
                    level.setCompleted(false);
                    level.setRemainingDays(100); // Or a different default
                }
            } else {
                try {
                    int score = Integer.parseInt(levelValue);
                    level.setScore(score);
                    level.setCompleted(true);
                    level.setRemainingDays(0.1); // or any other value to mark completion
                } catch (NumberFormatException e) {
                    log.warn("Invalid score format for level {}: {}", levelName, levelValue);
                    level.setScore(null);
                    level.setCompleted(false);
                    level.setRemainingDays(100); // Set a default value or handle as needed
                }
            }
        } else {
            level.setScore(null);
            level.setCompleted(false);
            level.setRemainingDays(100); // Default if no value is present
        }

        levelsData.add(level);
    }

    // Error handler
    private StudentProfileDto createErrorResponse(boolean success, String message) {
        StudentProfileDto profileData = new StudentProfileDto();
        profileData.setSuccess(success); // Set success to false
        profileData.setMessage(message); // Set the error message
        return profileData;
    }
}