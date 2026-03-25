package wallet.model;

import java.util.Objects;

public class WalletCredential {
    private final String walletId;
    private final String password;

    public WalletCredential(String walletId, String password) {
        this.walletId = walletId;
        this.password = password;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getPasswordHash() {
        return password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WalletCredential)) return false;
        WalletCredential that = (WalletCredential) obj;
        return Objects.equals(walletId, that.walletId);
    }
}
