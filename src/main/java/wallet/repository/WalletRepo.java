package wallet.repository;

import wallet.model.Wallet;

public interface WalletRepo {
    void save(Wallet wallet);

    boolean update(Wallet wallet);

    Wallet findByUserId(String userId);

    Wallet findByWalletId(String walletId);

}
