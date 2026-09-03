package com.PhonePay.Clone.service;
import com.PhonePay.Clone.entity.User;
import com.PhonePay.Clone.entity.Wallet;
import com.PhonePay.Clone.entity.Transaction;
import com.PhonePay.Clone.repository.UserRepository;
import com.PhonePay.Clone.repository.WalletRepository;
import com.PhonePay.Clone.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional // CRITICAL: Dono wallets ka balance ek sath update hona chahiye, nahi toh rollback!
    public String transferMoney(Long senderUserId, String receiverPhone, BigDecimal amount, String mpin) {
        // 1. Sender ka Wallet dhoondhein
        Wallet senderWallet = walletRepository.findByUserId(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender wallet not found!"));
        // 2. Validate Sender MPIN
        if (!senderWallet.getMpin().equals(mpin)) {
            throw new RuntimeException("Incorrect MPIN! Transaction Failed.");
        }
        // 3. Check Sender Balance (amount sender ke balance se zyada nahi hona chahiye)
        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Balance!");
        }
        // 4. Receiver ka User aur Wallet dhoondhein phone number se
        User receiverUser = userRepository.findByPhoneNumber(receiverPhone)
                .orElseThrow(() -> new RuntimeException("Receiver phone number not registered on PhonePe!"));
        Wallet receiverWallet = walletRepository.findByUser(receiverUser)
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found!"));
        // 5. Apne aap ko paise bhejna block karein
        if (senderWallet.getId().equals(receiverWallet.getId())) {
            throw new RuntimeException("Cannot transfer money to your own number!");
        }
        // 6. Balance Update Karein (Deduction & Credit)
        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        // 7. Transaction History Log Save Karein
        Transaction transaction = new Transaction();
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(receiverWallet);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");
        transaction.setDescription("Sent to " + receiverUser.getName());
        transactionRepository.save(transaction);
        return "Transaction Successful! ₹" + amount + " sent to " + receiverUser.getName();
    }
    // Upar WalletRepository autowired hona chahiye
    public List<Transaction> getTransactionHistory(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found!"));
        // Wallet object ko dono params mein bhej rahe hain (for Sender OR Receiver check)
        return transactionRepository.findBySenderWalletOrReceiverWalletOrderByTimestampDesc(wallet, wallet);
    }
}