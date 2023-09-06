package com.example.tema5.repository;

import com.example.tema5.model.Transaction;
import com.example.tema5.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
