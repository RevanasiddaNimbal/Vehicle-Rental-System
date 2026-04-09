package payment.strategy;

import payment.dto.PaymentDetails;

public interface PaymentStrategy<T extends PaymentDetails> {

    boolean pay(String customerId, double amount, T details);

}
