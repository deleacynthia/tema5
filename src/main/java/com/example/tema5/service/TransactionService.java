package com.example.tema5.service;

import com.example.tema5.model.Transaction;
import com.example.tema5.repository.TransactionRepository;
import com.example.tema5.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
    }
    public Transaction save(Transaction transaction) {

        if (transaction.getAmount() == null) {
            throw new RuntimeException("amount cannot be null.");
        }
        if (transaction.getCommissionPercent() == null && transaction.getCommissionAmount() == null) {
            throw new RuntimeException("missing commission details");
        }
        if (transaction.getCommissionPercent() == null) {
            throw new RuntimeException("Commission percentage cannot be null.");
        }
        if (transaction.getCommissionAmount() == null) {
            throw new RuntimeException("Commission amount cannot be null.");
        }
        if (transaction.getCurrency() == null) {
            throw new RuntimeException("Currency cannot be null.");
        }
        if (transaction.getCreatedAt() == null) {
            throw new RuntimeException("Creation date cannot be null.");
        }

        if (transaction.getAmount() < 0) {
            throw new RuntimeException("User cannot pay a negative amount.");
        }


        validateCurrency(transaction.getCurrency());

        validateTransactionDate(transaction.getCreatedAt());

        return transactionRepository.save(transaction);
    }


    private void validateCurrency(String currency) {
        List<String> validCurrencies = Arrays.asList("USD", "EUR", "GBP", "RON", "BGN");
        if (!validCurrencies.contains(currency)) {
            throw new RuntimeException("invalid currency type");
        }
    }
    private void validateTransactionDate(LocalDateTime transactionDate) {
        if (transactionDate == null) {
            throw new RuntimeException("Unexpected error occurred");
        } else if (transactionDate.isBefore(LocalDateTime.now().minusSeconds(1))) {
            throw new RuntimeException("You cannot make a past payment");
        }
    }


    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("transaction not found"));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    public Transaction update(Transaction transaction) {
        if (transaction.getId() == null) {
            throw new RuntimeException("Transaction ID is required for update");
        }
        return transactionRepository.save(transaction);
    }

}
