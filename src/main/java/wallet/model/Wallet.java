package wallet.model;

public class Wallet {
    private final String walletId;
    private final String userId;
    private double balance;

    public Wallet(String walletId, String userId, double balance) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
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
        if (this.balance < amount) throw new IllegalArgumentException("Insufficient balance");
        this.balance -= amount;
    }

    private void validateAmount(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
    }

}
