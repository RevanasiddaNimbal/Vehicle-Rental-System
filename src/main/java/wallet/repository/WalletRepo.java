package wallet.repository;

import wallet.model.Wallet;

public interface WalletRepo {
    boolean save(Wallet wallet);

    boolean update(Wallet wallet);

    Wallet findByUserId(String userId);

    Wallet findByWalletId(String walletId);

}
