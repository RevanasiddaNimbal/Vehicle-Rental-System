package wallet.service;

import util.PasswordUtil;
import wallet.model.WalletCredential;
import wallet.repository.WalletCredentialRepo;

public class WalletCredentialService {

    private final WalletCredentialRepo walletCredentialRepo;

    public WalletCredentialService(WalletCredentialRepo walletCredentialRepo) {
        this.walletCredentialRepo = walletCredentialRepo;
    }

    public void defaultWalletCredential(String walletId) {
        WalletCredential walletCredential = walletCredentialRepo.findByWalletId(walletId);
        if (walletCredential == null) {
            WalletCredential credential = new WalletCredential(walletId, "123456");
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

    public void changePassword(String walletId, String oldPassword, String newPassword) {

        WalletCredential credential = walletCredentialRepo.findByWalletId(walletId);

        if (credential == null) {
            System.out.println("Credential not found");
            return;
        }

        if (!PasswordUtil.verify(oldPassword, credential.getPasswordHash())) {
            System.out.println("Old password does not match");
            return;
        }

        String newHash = PasswordUtil.getHashPassword(newPassword);
        WalletCredential newCredential = new WalletCredential(walletId, newHash);
        walletCredentialRepo.update(newCredential);
    }

}