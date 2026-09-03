package com.PhonePay.Clone.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_wallet_id", referencedColumnName = "id")
    private com.PhonePay.Clone.entity.Wallet senderWallet;

    @ManyToOne
    @JoinColumn(name = "receiver_wallet_id", referencedColumnName = "id")
    private com.PhonePay.Clone.entity.Wallet receiverWallet;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED

    @Column(nullable = false)
    private String description; // e.g., "Sent to Amit"

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}