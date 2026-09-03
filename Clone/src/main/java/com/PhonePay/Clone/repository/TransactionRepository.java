package com.PhonePay.Clone.repository;

import com.PhonePay.Clone.entity.Transaction;
import com.PhonePay.Clone.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Sender ya Receiver dono mein se koi bhi agar user ka wallet hai, toh list fetch karein
    List<Transaction> findBySenderWalletOrReceiverWalletOrderByTimestampDesc(Wallet sender, Wallet receiver);
}