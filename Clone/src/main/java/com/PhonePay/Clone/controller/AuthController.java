package com.PhonePay.Clone.controller;
import com.PhonePay.Clone.entity.User;
import com.PhonePay.Clone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    // Upar JwtUtil autowire karein
    @Autowired
    private com.PhonePay.Clone.config.JwtUtil jwtUtil;
    // Register Endpoint: POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        try {
            // Request data se fields nikalna
            User user = new User();
            user.setName(request.get("name"));
            user.setPhoneNumber(request.get("phoneNumber"));
            user.setEmail(request.get("email"));
            user.setPassword(request.get("password")); // Android se jo password aayega
            String mpin = request.get("mpin");
            String response = userService.registerUser(user, mpin);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Login Endpoint: POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String phoneNumber = request.get("phoneNumber");
            String password = request.get("password");
            User user = userService.loginUser(phoneNumber, password);
            // Abhi ke liye hum simple user object return kar rahe hain, baad mein JWT token bhej sakte hain
            // 🔴 Login successful hone par JWT Token generate karein
            String token = jwtUtil.generateToken(user.getPhoneNumber());
            // Response mein User Details aur Token dono bhejenge
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("id", user.getId());
            responseData.put("name", user.getName());
            responseData.put("phoneNumber", user.getPhoneNumber());
            responseData.put("token", token); // Token client (Android) ko bhej diya
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Balance Check Endpoint: GET http://localhost:8080/api/auth/balance/{userId}
    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getBalance(@PathVariable Long userId) {
        try {
            com.PhonePay.Clone.entity.Wallet wallet = userService.getWalletByUserId(userId);
            // Sirf balance return karne ke liye ek map bana lete hain
            return ResponseEntity.ok(Map.of("balance", wallet.getBalance()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}