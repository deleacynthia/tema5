package com.example.tema5.service;
import com.example.tema5.model.Wallet;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WalletServiceTest {
    @Autowired
    private WalletService walletService;

    @Test
    public void testAmountHigherThanBalance() {
        Wallet sourceWallet = new Wallet(1L, "sourceWallet", 1L, 50.0, "USD");
        Wallet destinationWallet = new Wallet(2L, "destinationWallet", 2L, 0.0, "USD");
        Double transactionAmount = 100.0;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            walletService.transfer(sourceWallet, destinationWallet, transactionAmount);
        });

        String expectedMessage = "Transaction cannot be processed if amount is higher than balance";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testDestinationWalletDoesNotExist() {
        Wallet sourceWallet = new Wallet(1L, "sourceWallet",1L, 50.0, "USD");
        double transactionAmount = 10.0;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            walletService.transfer(sourceWallet, null, transactionAmount);
        });

        String expectedMessage = "Destination wallet doesn’t exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSourceWalletDoesNotExist() {
        Wallet destinationWallet = new Wallet(2L, "destinationWallet",1L, 0.0, "USD");
        double transactionAmount = 10.0;

        Exception exception = assertThrows(RuntimeException.class, () -> {
            walletService.transfer(null, destinationWallet, transactionAmount);
        });

        String expectedMessage = "Source wallet doesn’t exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testWalletNotAssociatedWithUser() {
        Wallet walletWithoutUser = new Wallet(1L, "noUserWallet", null, 50.0, "USD");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            walletService.checkUserAssociation(walletWithoutUser);
        });

        String expectedMessage = "Wallet isn’t associated with a user";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUserCannotPayItself() {
        Wallet userWallet = new Wallet(1L, "userWallet", 1L, 50.0, "USD");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            walletService.transfer(userWallet, userWallet, 10.0);
        });

        String expectedMessage = "User cannot pay itself";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }



}
