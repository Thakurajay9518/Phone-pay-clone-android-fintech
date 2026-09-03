package com.PhonePay.Clone.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Request ke Header se "Authorization" check karna
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // JWT Token hamesha "Bearer [token_string]" ke format mein hota hai
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " hata kar token nikalna
            try {
                username = jwtUtil.extractPhoneNumber(jwt);
            } catch (Exception e) {
                System.out.println("JWT Token extract karne mein dikkat aayi: " + e.getMessage());
//                logger.error("JWT Token extract karne mein dikkat aayi: " + e.getMessage());
            }
        }

        // 2. Agar token valid hai aur security context set nahi hai, toh user ko authenticate karna
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(jwt, username)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>()); // Roles khali rakh rahe hain abhi
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Spring security ko batana ki ye request safe aur authenticated hai
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}