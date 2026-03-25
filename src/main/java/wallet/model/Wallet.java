package wallet.model;

import java.util.Objects;

public class Wallet {
    private String walletId;
    private final String userId;
    private double balance;

    public Wallet(String walletId, String userId, double balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getWalletId() {
        return walletId;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void credit(double amount) {
        validateAmount(amount);
        this.balance += amount;
    }

    public void debit(double amount) {
        validateAmount(amount);
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance -= amount;
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Wallet)) return false;
        Wallet wallet = (Wallet) obj;
        return Objects.equals(walletId, wallet.walletId);
    }
}