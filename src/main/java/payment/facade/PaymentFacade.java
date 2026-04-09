package payment.facade;

import payment.dto.PaymentDetails;
import payment.strategy.PaymentStrategy;
import penalty.model.Penalty;
import rental.model.Rental;
import transaction.model.TransactionStatus;
import transaction.model.TransactionType;
import transaction.service.TransactionService;
import vehicle.models.Vehicle;
import vehicle.service.VehicleService;
import wallet.model.Wallet;
import wallet.service.WalletService;

import java.util.List;

public class PaymentFacade {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final VehicleService vehicleService;

    public static final String ESCROW_WALLET = "SYSTEM-ESCROW";
    public static final String REVENUE_WALLET = "SYSTEM-REVENUE";
    private static final double SYSTEM_COMMISSION_RATE = 0.10;

    public PaymentFacade(WalletService walletService, TransactionService transactionService, VehicleService vehicleService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.vehicleService = vehicleService;
    }

    public <T extends PaymentDetails> boolean processBookingPayments(String customerId, List<Rental> rentals, PaymentStrategy<T> paymentStrategy, T details) {

        double grandTotal = rentals.stream()
                .mapToDouble(r -> r.getTotalPrice() + r.getSecurityDeposit())
                .sum();

        boolean isPaid;
        try {
            isPaid = paymentStrategy.pay(customerId, grandTotal, details);
        } catch (Exception e) {
            return false;
        }

        if (!isPaid) return false;

        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        for (Rental rental : rentals) {

            String rentalId = String.valueOf(rental.getId());

            String fareTxId = safeLog(
                    customerWallet.getWalletId(),
                    ESCROW_WALLET,
                    rental.getTotalPrice(),
                    TransactionType.RENTAL_FARE,
                    TransactionStatus.PENDING,
                    rentalId,
                    "Rental Fare Payment"
            );

            try {
                walletService.executeSystemTransfer(customerWallet.getWalletId(), ESCROW_WALLET, rental.getTotalPrice());
                transactionService.updateTransactionStatus(fareTxId, TransactionStatus.SUCCESS);
            } catch (Exception e) {
                transactionService.updateTransactionStatus(fareTxId, TransactionStatus.FAILED);
                throw new RuntimeException("Rental fare transfer failed");
            }

            String depositTxId = safeLog(
                    customerWallet.getWalletId(),
                    ESCROW_WALLET,
                    rental.getSecurityDeposit(),
                    TransactionType.SECURITY_DEPOSIT,
                    TransactionStatus.PENDING,
                    rentalId,
                    "Deposit Payment"
            );

            try {
                walletService.executeSystemTransfer(customerWallet.getWalletId(), ESCROW_WALLET, rental.getSecurityDeposit());
                transactionService.updateTransactionStatus(depositTxId, TransactionStatus.SUCCESS);
            } catch (Exception e) {
                transactionService.updateTransactionStatus(depositTxId, TransactionStatus.FAILED);
                throw new RuntimeException("Deposit transfer failed");
            }
        }

        return true;
    }

    public void refundWithRetry(String customerId, List<Rental> rentals, int retries) {

        double total = rentals.stream()
                .mapToDouble(r -> r.getTotalPrice() + r.getSecurityDeposit())
                .sum();

        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        int attempt = 0;

        while (attempt < retries) {

            String txId = safeLog(ESCROW_WALLET,
                    customerWallet.getWalletId(),
                    total,
                    TransactionType.REFUND,
                    TransactionStatus.PENDING,
                    "N/A",
                    "Refund Payment"
            );

            try {
                walletService.executeSystemTransfer(ESCROW_WALLET, customerWallet.getWalletId(), total);
                transactionService.updateTransactionStatus(txId, TransactionStatus.SUCCESS);
                return;

            } catch (Exception e) {
                transactionService.updateTransactionStatus(txId, TransactionStatus.FAILED);
                attempt++;

                if (attempt == retries) {
                    throw new RuntimeException("Refund failed");
                }
            }
        }
    }

    private String safeLog(String from, String to, double amount,
                           TransactionType type, TransactionStatus status,
                           String rentalId, String desc) {

        int attempt = 0;

        while (attempt < 3) {
            try {
                return transactionService.logTransfer(from, to, amount, type, status, rentalId, desc);
            } catch (Exception e) {
                attempt++;
                if (attempt == 3) {
                    throw new RuntimeException("Transaction logging failed");
                }
            }
        }
        throw new RuntimeException("Logging failed");
    }

    public void processReturnPayouts(String customerId, List<Rental> rentals, List<Penalty> penalties) {

        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        for (Rental rental : rentals) {

            String rentalId = String.valueOf(rental.getId());
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            Wallet ownerWallet = walletService.getWalletByUserId(vehicle.getOwnerId());

            double penaltyAmount = penalties == null ? 0 :
                    penalties.stream()
                            .filter(p -> String.valueOf(p.getRentalId()).equals(rentalId))
                            .mapToDouble(Penalty::getPenaltyAmount)
                            .sum();

            double deducted = Math.min(penaltyAmount, rental.getSecurityDeposit());
            double refund = rental.getSecurityDeposit() - deducted;
            double penaltyCommission = deducted * SYSTEM_COMMISSION_RATE;
            double penaltyRefund = deducted - penaltyCommission;
            double commission = rental.getTotalPrice() * SYSTEM_COMMISSION_RATE + penaltyCommission;
            double ownerPayout = rental.getTotalPrice() - commission;

            processSingleTransaction(ESCROW_WALLET, ownerWallet.getWalletId(), ownerPayout,
                    TransactionType.OWNER_PAYOUT, rentalId, "Owner Payout");

            processSingleTransaction(ESCROW_WALLET, REVENUE_WALLET, commission,
                    TransactionType.SYSTEM_COMMISSION, rentalId, "Commission");


            if (penaltyRefund > 0) {
                processSingleTransaction(ESCROW_WALLET, customerWallet.getWalletId(), penaltyRefund,
                        TransactionType.PENALTY_CHARGE, rentalId, "Penalty Amount");
            }

            if (refund > 0) {
                processSingleTransaction(ESCROW_WALLET, customerWallet.getWalletId(), refund,
                        TransactionType.REFUND, rentalId, "Deposit Refund");
            }
        }
    }

    public void processCancellationRefund(String customerId, Rental rental, double refundAmount) {

        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        String rentalId = String.valueOf(rental.getId());

        double rentalPrice = rental.getTotalPrice();
        double deposit = rental.getSecurityDeposit();

        double rentalRefund = Math.min(refundAmount, rentalPrice);
        double cancellationFee = rentalPrice - rentalRefund;
        double cancellationCommission = cancellationFee * SYSTEM_COMMISSION_RATE;
        cancellationFee -= cancellationCommission;

        double totalCustomerRefund = rentalRefund + deposit;

        if (totalCustomerRefund > 0) {
            processSingleTransaction(ESCROW_WALLET, customerWallet.getWalletId(),
                    totalCustomerRefund,
                    TransactionType.REFUND,
                    rentalId,
                    "Cancellation Refund");
        }

        if (cancellationCommission > 0) {
            processSingleTransaction(ESCROW_WALLET, REVENUE_WALLET,
                    cancellationCommission,
                    TransactionType.CANCELLATION_FEE,
                    rentalId,
                    "Cancellation commission");
        }
        if (cancellationFee > 0) {
            processSingleTransaction(ESCROW_WALLET, customerWallet.getWalletId(),
                    cancellationFee,
                    TransactionType.CANCELLATION_FEE,
                    rentalId,
                    "Cancellation Fee");
        }
    }

    private void processSingleTransaction(String from, String to, double amount,
                                          TransactionType type, String rentalId, String desc) {

        String txId = safeLog(from, to, amount, type, TransactionStatus.PENDING, rentalId, desc);

        try {
            walletService.executeSystemTransfer(from, to, amount);
            transactionService.updateTransactionStatus(txId, TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transactionService.updateTransactionStatus(txId, TransactionStatus.FAILED);
            throw new RuntimeException("Transaction failed");
        }
    }
}