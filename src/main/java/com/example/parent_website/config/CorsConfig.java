//package com.example.parent_website.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:4202"); // Replace with your Angular app's address
//        config.addAllowedOrigin("http://192.168.1.102:4202");
//        config.addAllowedOrigin("http://192.168.1.101:4202");
//        config.addAllowedOrigin("http://192.168.137.1:4202");
//        config.addAllowedOrigin("http://192.168.205.87:4202");
//        config.addAllowedOrigin("http://192.168.203.87:4202");
//        config.addAllowedOrigin("http://192.168.80.87:4202");
//        config.addAllowedOrigin("http://10.160.87.211:4202");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//}