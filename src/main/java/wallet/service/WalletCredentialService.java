package wallet.service;

import util.InputUtil;
import util.PasswordUtil;
import wallet.model.WalletCredential;
import wallet.repository.WalletCredentialRepo;

import java.util.Scanner;

public class WalletCredentialService {

    private final WalletCredentialRepo walletCredentialRepo;

    public WalletCredentialService(WalletCredentialRepo walletCredentialRepo) {
        this.walletCredentialRepo = walletCredentialRepo;
    }

    public WalletCredential getWalletCredential(String walletId) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential == null) {
            return null;
        }
        return walletCredential;
    }

    public void defaultWalletCredential(String walletId) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential == null) {
            WalletCredential credential = new WalletCredential(walletId, PasswordUtil.getHashPassword("123456"));
            walletCredentialRepo.save(credential);
            System.out.println("default wallet credential created successfully");
        }
    }

    public void createWalletCredential(String walletId, String password) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential != null) {
            System.out.println("Wallet Credential already exists");
            return;
        }

        String hashPassword = PasswordUtil.getHashPassword(password);

        WalletCredential credential = new WalletCredential(walletId, hashPassword);
        walletCredentialRepo.save(credential);
    }

    public boolean verifyWalletPassword(String walletId, String password) {

        WalletCredential credential = walletCredentialRepo.findByWalletId(walletId);
        if (credential == null) {
            System.out.println("Credential not found");
            return false;
        }
        return PasswordUtil.verify(password, credential.getPasswordHash());
    }

    public boolean changePassword(Scanner input, String walletId) {

        WalletCredential credential = walletCredentialRepo.findByWalletId(walletId);

        if (credential == null) {
            System.out.println("Credential not found");
            return false;
        }

        String oldPassword = InputUtil.readValidPassword(input, "Enter old password");
        String newPassword = InputUtil.readValidPassword(input, "Enter new password");

        if (!PasswordUtil.verify(oldPassword, credential.getPasswordHash())) {
            System.out.println("Old password does not match");
            return false;
        }

        String newHash = PasswordUtil.getHashPassword(newPassword);
        WalletCredential newCredential = new WalletCredential(walletId, newHash);
        return walletCredentialRepo.update(newCredential);
    }

}