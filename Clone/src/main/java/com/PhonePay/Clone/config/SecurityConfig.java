package com.PhonePay.Clone.config;

import com.PhonePay.Clone.config.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**")
//                        .permitAll().anyRequest().authenticated())
//                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
//    }
//


    // Password aur MPIN encrypt karne ke liye BCrypt encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Stateless APIs ke liye CSRF disable karte hain
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll() // Inhe allow karein
                        .anyRequest().authenticated() // Baaki saari APIs (Balance, Transfer) locked rahengi
                )
                // 🔴 Stateless Session management kyunki hum REST APIs bana rahe hain (No cookies)
                .sessionManagement(session -> session.setSessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 🔴 Apne custom JWT filter ko standard UsernamePasswordAuthenticationFilter se PEHLE add karna
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}