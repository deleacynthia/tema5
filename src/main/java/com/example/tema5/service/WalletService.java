package com.example.tema5.service;

import com.example.tema5.model.Wallet;
import com.example.tema5.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void transfer(Wallet sourceWallet, Wallet destinationWallet, Double amount) {

        if (destinationWallet == null) {
            throw new RuntimeException("Destination wallet doesn’t exist");
        }
        if (sourceWallet == null) {
            throw new RuntimeException("Source wallet doesn’t exist");
        }
        if (sourceWallet.getBalance() < amount) {
            throw new RuntimeException("Transaction cannot be processed if amount is higher than balance");
        }

        if (sourceWallet.getId().equals(destinationWallet.getId())) {
            throw new RuntimeException("User cannot pay itself");
        }

        if (amount < 0) {
            throw new RuntimeException("User cannot pay a negative amount");
        }

        sourceWallet.setBalance(sourceWallet.getBalance()-amount);
        destinationWallet.setBalance(destinationWallet.getBalance()+amount);

        walletRepository.save(sourceWallet);
        walletRepository.save(destinationWallet);
    }

    public void checkUserAssociation(Wallet wallet) {
        if (wallet == null || wallet.getUser_id()== null) {
            throw new RuntimeException("Wallet isn’t associated with a user");
        }
    }
}
