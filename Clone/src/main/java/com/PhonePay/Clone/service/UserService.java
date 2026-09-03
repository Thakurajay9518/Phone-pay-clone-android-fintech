package com.PhonePay.Clone.service;
import com.PhonePay.Clone.entity.User;
import com.PhonePay.Clone.entity.Wallet;
import com.PhonePay.Clone.repository.UserRepository;
import com.PhonePay.Clone.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
@Service
public class UserService {
    // Upar Autowired dependency add karein
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    // @Transactional ka matlab hai agar wallet banane mein error aaya,
    // toh user bhi save nahi hoga (Rollback ho jayega). Fintech mein ye bohot zaroori hai!
    @Transactional
    public String registerUser(User user, String mpin) {
        // 1. Check karein ki number pehle se registered toh nahi hai
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered!");
        }
        // 🔴 ENCRYPTION HERE
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 2. User ko save karein
        // Note: Production mein password ko BCrypt se encode karte hain, abhi hum simple rakh rahe hain
        User savedUser = userRepository.save(user);
        // 3. User ke liye automatic ek Wallet create karein
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO); // Shuruat mein 0 balance
//        wallet.setMpin(mpin); // Set 4 ya 6 digit MPIN
        // 🔴 MPIN ENCRYPTION HERE
        wallet.setMpin(passwordEncoder.encode(mpin));
        walletRepository.save(wallet);
        return "User and Wallet registered successfully!";
    }
    // Login check karne ke liye method
    public User loginUser(String phoneNumber, String password) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found with this phone number!"));
        // Plain text check karne ke bajaye BCrypt ka matches method use karein
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }
//        if (!user.getPassword().equals(password)) {
//            throw new RuntimeException("Incorrect password!");
//        }
        return user; // Agar sahi hai toh user data return karein
    }
    // User ID ke base par wallet balance nikalne ke liye method
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for this user!"));
    }
}