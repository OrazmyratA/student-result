package com.example.parent_website;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParentWebsiteApplication {

	public static void main(String[] args) {
      //  System.out.println("Classpath: " + Arrays.toString(System.getProperty("java.class.path").split(System.getProperty("path.separator"))));  // Add this line for debugging

		SpringApplication.run(ParentWebsiteApplication.class, args);
	}

}
