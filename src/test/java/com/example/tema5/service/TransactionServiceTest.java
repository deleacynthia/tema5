package com.example.tema5.service;

import com.example.tema5.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction(null, 100.0, 2.0, 2.0, "USD", LocalDateTime.now().plusSeconds(1));
        Transaction savedTransaction = transactionService.save(transaction);
        assertNotNull(savedTransaction.getId());
    }


    @Test
    public void testFindTransactionById() {
        Transaction transaction = new Transaction(null, 100.0, 2.0, 2.0, "USD", LocalDateTime.now());
        Transaction savedTransaction = transactionService.save(transaction);
        Transaction fetchedTransaction = transactionService.findById(savedTransaction.getId());
        assertEquals(savedTransaction.getId(), fetchedTransaction.getId());
    }

    @Test
    public void testFindAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        assertNotNull(transactions);
    }

    @Test
    public void testDeleteTransaction() {
        LocalDateTime futureDate = LocalDateTime.now().plusSeconds(10);
        Transaction transaction = new Transaction(null, 100.0, 2.0, 2.0, "USD", futureDate);

        Transaction savedTransaction = transactionService.save(transaction);
        transactionService.delete(savedTransaction.getId());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.findById(savedTransaction.getId());
        });
        assertTrue(exception.getMessage().contains("transaction not found"));
    }

    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(null, 100.0, 2.0, 2.0, "USD", LocalDateTime.now());
        Transaction savedTransaction = transactionService.save(transaction);
        savedTransaction.setAmount(200.0);
        Transaction updatedTransaction = transactionService.update(savedTransaction);
        assertEquals(200.0, updatedTransaction.getAmount());
    }


    @Test
    public void testCreateTransactionWithPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        Transaction transaction = new Transaction(null, 100.0, 1.0, 1.0, "USD", pastDate);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.save(transaction);
        });
        assertTrue(exception.getMessage().contains("You cannot make a past payment"));
    }

    @Test
    public void testCreateTransactionWithNegativeAmount() {
        Transaction transaction = new Transaction(null, -100.0, 1.0, 1.0, "USD", LocalDateTime.now());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.save(transaction);
        });
        assertTrue(exception.getMessage().contains("User cannot pay a negative amount"));
    }

    @Test
    public void testInvalidCurrencyForTransaction() {
        Transaction transaction = new Transaction(null, 100.0, 1.0, 1.0, "fgh", LocalDateTime.now());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.save(transaction);
        });
        assertTrue(exception.getMessage().contains("invalid currency type"));
    }

    @Test
    public void testUnexpectedTransactionError() {
        Transaction transaction = new Transaction(null, null, null, null, null, null);
        try {
            transactionService.save(transaction);
            fail("fail message");
        } catch (RuntimeException ex) {
            assertEquals("amount cannot be null.", ex.getMessage());
        }
    }

    @Test
    public void testTransactionWithoutCommissionDetails() {
        Transaction transaction = new Transaction(null, 100.0, null, null, "USD", LocalDateTime.now());
        try {
            transactionService.save(transaction);
            fail("fail message");
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().contains("missing commission details"));
        }
    }





}
