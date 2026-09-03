package com.PhonePay.Clone.repository;

import com.PhonePay.Clone.entity.Wallet;
import com.PhonePay.Clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    // User object ke base par wallet find karne ke liye
    Optional<Wallet> findByUser(User user);

    // Direct User Id ke base par wallet find karne ke liye
    Optional<Wallet> findByUserId(Long userId);
}