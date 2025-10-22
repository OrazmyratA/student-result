package com.example.parent_website.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileDto {
    private String username;
    private String firstName;
    private String lastName;
    private Integer score;
    private Integer point;
    private double rankingPercentage;
    private List<LevelDto> levels;
    private boolean success = true; // Add the success field
    private String message; // Add the message field
}