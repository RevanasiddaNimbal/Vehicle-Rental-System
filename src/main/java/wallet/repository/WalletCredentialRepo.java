package wallet.repository;

import wallet.model.WalletCredential;

public interface WalletCredentialRepo {
    boolean save(WalletCredential walletCredentials);

    boolean update(WalletCredential walletCredentials);

    WalletCredential findByWalletId(String walletId);
}
