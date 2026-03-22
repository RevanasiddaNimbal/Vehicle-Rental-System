package wallet.repository;

import wallet.model.WalletCredential;

import java.util.HashMap;
import java.util.Map;

public class WalletCredentialMemoryRepo implements WalletCredentialRepo {
    Map<String, WalletCredential> storage = new HashMap<>();

    @Override
    public boolean save(WalletCredential walletCredential) {
        storage.put(walletCredential.getWalletId(), walletCredential);
        return true;
    }

    @Override
    public boolean update(WalletCredential walletCredential) {
        if (storage.get(walletCredential.getWalletId()) == null) return false;
        storage.put(walletCredential.getWalletId(), walletCredential);
        return true;
    }

    @Override
    public WalletCredential findByWalletId(String walletId) {
        return storage.get(walletId);
    }
}
