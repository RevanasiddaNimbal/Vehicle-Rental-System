package wallet.model;

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
}
