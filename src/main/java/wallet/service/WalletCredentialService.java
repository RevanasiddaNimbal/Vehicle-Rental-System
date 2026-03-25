package wallet.service;

import exception.InvalidCredentialsException;
import exception.ResourceNotFoundException;
import util.PasswordUtil;
import wallet.model.WalletCredential;
import wallet.repository.WalletCredentialRepo;

public class WalletCredentialService {

    private final WalletCredentialRepo walletCredentialRepo;

    public WalletCredentialService(WalletCredentialRepo walletCredentialRepo) {
        this.walletCredentialRepo = walletCredentialRepo;
    }

    public WalletCredential getWalletCredential(String walletId) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential == null) {
            throw new ResourceNotFoundException("Wallet credentials not found for ID: " + walletId);
        }
        return walletCredential;
    }

    public void defaultWalletCredential(String walletId) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential == null) {
            WalletCredential credential = new WalletCredential(walletId, PasswordUtil.getHashPassword("123456"));
            walletCredentialRepo.save(credential);
        }
    }

    public void createWalletCredential(String walletId, String password) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential != null) {
            throw new IllegalStateException("Wallet Credential already exists for this wallet.");
        }

        String hashPassword = PasswordUtil.getHashPassword(password);
        WalletCredential credential = new WalletCredential(walletId, hashPassword);
        walletCredentialRepo.save(credential);
    }

    public boolean verifyWalletPassword(String walletId, String password) {
        WalletCredential credential = walletCredentialRepo.findByWalletId(walletId);
        if (credential == null) {
            throw new ResourceNotFoundException("Wallet credentials not found.");
        }
        return PasswordUtil.verify(password, credential.getPasswordHash());
    }

    public void changePassword(String walletId, String oldPassword, String newPassword) {
        WalletCredential credential = walletCredentialRepo.findByWalletId(walletId);

        if (credential == null) {
            throw new ResourceNotFoundException("Wallet credentials not found.");
        }

        if (!PasswordUtil.verify(oldPassword, credential.getPasswordHash())) {
            throw new InvalidCredentialsException("Old password does not match.");
        }

        String newHash = PasswordUtil.getHashPassword(newPassword);
        WalletCredential newCredential = new WalletCredential(walletId, newHash);
        walletCredentialRepo.update(newCredential);
    }
}