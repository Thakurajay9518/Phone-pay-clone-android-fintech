package com.PhonePay.Clone.repository;

import com.PhonePay.Clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find user by phone number for login/transactions
    Optional<User> findByPhoneNumber(String phoneNumber);

    // Check karne ke liye ki phone number pehle se registered toh nahi hai
    boolean existsByPhoneNumber(String phoneNumber);
}