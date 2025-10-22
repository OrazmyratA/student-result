package com.example.parent_website.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LevelDto {
    private String name;
    private Integer score;
    private boolean completed;
    private double remainingDays;
}