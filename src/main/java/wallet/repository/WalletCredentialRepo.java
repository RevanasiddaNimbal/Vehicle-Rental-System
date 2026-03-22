package wallet.repository;

import wallet.model.WalletCredential;

public interface WalletCredentialRepo {
    void save(WalletCredential walletCredentials);

    boolean update(WalletCredential walletCredentials);

    WalletCredential findByWalletId(String walletId);
}
