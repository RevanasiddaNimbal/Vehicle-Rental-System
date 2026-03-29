package payment.facade;

import payment.dto.PaymentDetails;
import payment.stretegy.PaymentStrategy;
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
import java.util.concurrent.CompletableFuture;

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
        double grandTotal = 0.0;
        for (Rental rental : rentals) {
            grandTotal += rental.getTotalPrice() + rental.getSecurityDeposit();
        }

        boolean isPaid = paymentStrategy.pay(customerId, grandTotal, details);
        if (!isPaid) {
            return false;
        }

        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        CompletableFuture.runAsync(() -> {
            for (Rental rental : rentals) {
                String rentalId = String.valueOf(rental.getId());
                transactionService.logTransfer(customerWallet.getWalletId(), ESCROW_WALLET, rental.getTotalPrice(),
                        TransactionType.RENTAL_FARE, TransactionStatus.SUCCESS, rentalId, "Rental Fare held in Escrow");
                transactionService.logTransfer(customerWallet.getWalletId(), ESCROW_WALLET, rental.getSecurityDeposit(),
                        TransactionType.SECURITY_DEPOSIT, TransactionStatus.SUCCESS, rentalId, "Security Deposit held in Escrow");
            }
        });

        return true;
    }

    public <T extends PaymentDetails> boolean processPayment(String customerId, double amount, PaymentStrategy<T> paymentStrategy, T details) {
        return paymentStrategy.pay(customerId, amount, details);
    }

    public void processRefund(String customerId, double amount) {
        Wallet customerWallet = walletService.getWalletByUserId(customerId);
        executeAndLogAsync(ESCROW_WALLET, customerWallet.getWalletId(), amount,
                TransactionType.REFUND, "N/A", "System Refund");
    }

    public void refundFailedBooking(String customerId, List<Rental> rentals) {
        double grandTotal = rentals.stream().mapToDouble(rental -> rental.getTotalPrice() + rental.getSecurityDeposit()).sum();

        Wallet customerWallet = walletService.getWalletByUserId(customerId);
        walletService.executeSystemTransfer(ESCROW_WALLET, customerWallet.getWalletId(), grandTotal);

        CompletableFuture.runAsync(() -> {
            transactionService.logTransfer(ESCROW_WALLET, customerWallet.getWalletId(), grandTotal,
                    TransactionType.REFUND, TransactionStatus.SUCCESS, "N/A", "System Rollback: Booking Failed");
        });
    }

    public void processReturnPayouts(String customerId, List<Rental> rentals, List<Penalty> penalties) {
        Wallet customerWallet = walletService.getWalletByUserId(customerId);

        for (Rental rental : rentals) {
            String rentalIdStr = String.valueOf(rental.getId());
            Vehicle vehicle = vehicleService.getVehiclesById(rental.getVehicleId());
            Wallet ownerWallet = walletService.getWalletByUserId(vehicle.getOwnerId());

            double commission = rental.getTotalPrice() * SYSTEM_COMMISSION_RATE;
            double ownerPayout = rental.getTotalPrice() - commission;

            try {
                executeAndLogAsync(ESCROW_WALLET, ownerWallet.getWalletId(), ownerPayout,
                        TransactionType.OWNER_PAYOUT, rentalIdStr, "Trip completed payout");
            } catch (Exception e) {
                sweepFailedPayoutToSystem(ESCROW_WALLET, REVENUE_WALLET, ownerPayout, rentalIdStr);
            }

            executeAndLogAsync(ESCROW_WALLET, REVENUE_WALLET, commission,
                    TransactionType.SYSTEM_COMMISSION, rentalIdStr, "Platform commission");

            double totalPenalty = 0.0;
            if (penalties != null) {
                for (Penalty p : penalties) {
                    if (String.valueOf(p.getRentalId()).equals(rentalIdStr)) {
                        totalPenalty += p.getPenaltyAmount();
                    }
                }
            }

            double actualPenaltyDeducted = Math.min(totalPenalty, rental.getSecurityDeposit());
            double refundAmount = rental.getSecurityDeposit() - actualPenaltyDeducted;

            if (actualPenaltyDeducted > 0) {
                executeAndLogAsync(ESCROW_WALLET, REVENUE_WALLET, actualPenaltyDeducted,
                        TransactionType.PENALTY_CHARGE, rentalIdStr, "Penalty deducted from deposit");
            }

            if (refundAmount > 0) {
                executeAndLogAsync(ESCROW_WALLET, customerWallet.getWalletId(), refundAmount,
                        TransactionType.REFUND, rentalIdStr, "Remaining Deposit Refunded");
            }
        }
    }

    public void processCancellationRefund(String customerId, Rental rental, double refundAmount) {
        Wallet customerWallet = walletService.getWalletByUserId(customerId);
        String rentalId = String.valueOf(rental.getId());

        double totalHeldInEscrow = rental.getTotalPrice() + rental.getSecurityDeposit();
        double cancellationFee = totalHeldInEscrow - refundAmount;

        if (refundAmount > 0) {
            executeAndLogAsync(ESCROW_WALLET, customerWallet.getWalletId(), refundAmount,
                    TransactionType.REFUND, rentalId, "Cancellation Refund");
        }

        if (cancellationFee > 0) {
            executeAndLogAsync(ESCROW_WALLET, REVENUE_WALLET, cancellationFee,
                    TransactionType.SYSTEM_COMMISSION, rentalId, "Cancellation Fee retained");
        }
    }

    public void sweepFailedPayoutToSystem(String escrowWalletId, String revenueWalletId, double amount, String rentalId) {
        executeAndLogAsync(escrowWalletId, revenueWalletId, amount,
                TransactionType.FAILED_PAYOUT_HOLD, rentalId, "SWEPT TO ADMIN: Payout Failed. Holding.");
    }

    public void resolveSweptPayout(String revenueWalletId, String ownerWalletId, double amount, String rentalId) {
        executeAndLogAsync(revenueWalletId, ownerWalletId, amount,
                TransactionType.MANUAL_RESOLUTION_PAYOUT, rentalId, "Admin Manually Resolved and Paid Owner");
    }

    private void executeAndLogAsync(String fromWallet, String toWallet, double amount, TransactionType type, String rentalId, String description) {
        walletService.executeSystemTransfer(fromWallet, toWallet, amount);
        CompletableFuture.runAsync(() -> {
            transactionService.logTransfer(fromWallet, toWallet, amount, type, TransactionStatus.SUCCESS, rentalId, description);
        });
    }
}