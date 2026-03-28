package payment.dto;

public class WalletPaymentDetails implements PaymentDetails {
    private final String pin;

    public WalletPaymentDetails(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }
}