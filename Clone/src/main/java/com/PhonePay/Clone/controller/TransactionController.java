package com.PhonePay.Clone.controller;

import com.PhonePay.Clone.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Endpoint: POST http://localhost:8080/api/transaction/transfer
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> request) {
        try {
            Long senderUserId = Long.valueOf(request.get("senderUserId").toString());
            String receiverPhone = (String) request.get("receiverPhone");
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String mpin = (String) request.get("mpin");

            String result = transactionService.transferMoney(senderUserId, receiverPhone, amount, mpin);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint: GET http://localhost:8080/api/transaction/history/{userId}
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getHistory(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionHistory(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}