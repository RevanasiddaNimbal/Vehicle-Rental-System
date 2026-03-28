package UI;

import transaction.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionPrinter implements UserPrinter<Transaction> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");

    @Override
    public void print(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n====================================================================================================================================================================");
        System.out.printf("%-15s | %-15s | %-15s | %-12s | %-20s | %-10s | %-30s | %-20s%n",
                "Transaction ID", "From Wallet", "To Wallet", "Amount", "Type", "Status", "Description", "Date & Time");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Transaction t : transactions) {
            String desc = t.getDescription() != null ? t.getDescription() : "-";

            if (desc.length() > 30) {
                desc = desc.substring(0, 27) + "...";
            }
            System.out.printf("%-15s | %-15s | %-15s | Rs. %-8.2f | %-19s  | %-10s | %-30s | %-20s%n",
                    t.getTransactionId(),
                    t.getSourceWalletId(),
                    t.getDestinationWalletId(),
                    t.getAmount(),
                    t.getType().name(),
                    t.getStatus().name(),
                    desc,
                    t.getTimestamp().format(FORMATTER));
        }
        System.out.println("====================================================================================================================================================================\n");
    }
}