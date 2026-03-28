package payment.stretegy;

import java.util.Scanner;

public interface PaymentStrategy {
    boolean pay(Scanner input, String customerId, double amount);
}
