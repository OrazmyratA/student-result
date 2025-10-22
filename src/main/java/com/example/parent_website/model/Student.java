package com.example.parent_website.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String className;
    private String englishLevel;
    private Long levelId;
    private Long testId;
    private Integer score;
    private Integer point;

    @Column(name = "`Let's go`") // Use backticks to escape the column name
    private String letsGo; // Store the score or percentage value as a String

    private String Beginner;
    private String Elementary;

    @Column(name = "Pre-Intermediate")
    private String preIntermediate;

    private String Intermediate;

    @Column(name = "Upper-Intermediate")
    private String upperIntermediate;

    private String Advance;

    private String createdAt;
    private String updatedAt;
	public Integer getPoint() {
		// TODO Auto-generated method stub
		return this.point;
	}
}