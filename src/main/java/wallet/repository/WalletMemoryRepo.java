package wallet.repository;

import wallet.model.Wallet;

import java.util.HashMap;
import java.util.Map;

public class WalletMemoryRepo implements WalletRepo {
    Map<String, Wallet> storage = new HashMap<>();

    @Override
    public boolean save(Wallet wallet) {
        storage.put(wallet.getWalletId(), wallet);
        return true;
    }

    @Override
    public boolean update(Wallet wallet) {
        if (storage.get(wallet.getWalletId()) == null) return false;
        storage.put(wallet.getWalletId(), wallet);
        return true;
    }

    @Override
    public Wallet findByUserId(String userId) {
        for (Wallet wallet : storage.values()) {
            if (wallet.getUserId().equals(userId)) {
                return wallet;
            }
        }
        return null;
    }

    @Override
    public Wallet findByWalletId(String walletId) {
        return storage.get(walletId);
    }


}
