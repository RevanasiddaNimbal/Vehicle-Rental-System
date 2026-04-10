package wallet.service;

import exception.InsufficientFundsException;
import exception.InvalidCredentialsException;
import exception.ResourceNotFoundException;
import transaction.service.TransactionService;
import util.IdGeneratorUtil;
import util.IdPrefix;
import wallet.model.Wallet;
import wallet.repository.WalletRepo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WalletService {
    private static final String SYSTEM_USER_ID = "ADM-002";
    private static final String REVENUE_WALLET_ID = "SYSTEM-REVENUE";
    private static final String ESCROW_WALLET_ID = "SYSTEM-ESCROW";
    private final WalletRepo walletRepo;
    private final WalletCredentialService walletCredentialService;
    private final TransactionService transactionService;

    private final ConcurrentHashMap<String, Lock> walletLocks = new ConcurrentHashMap<>();

    private static final double INITIAL_BALANCE = 2000.0;
    private static final double MIN_BALANCE_FOR_WITHDRAWAL = 1000.0;

    public WalletService(WalletRepo walletRepo, WalletCredentialService walletCredentialService, TransactionService transactionService) {
        this.walletRepo = walletRepo;
        this.walletCredentialService = walletCredentialService;
        this.transactionService = transactionService;
    }

    private Lock getWalletLock(String walletId) {
        return walletLocks.computeIfAbsent(walletId, k -> new ReentrantLock());
    }

    public Wallet getWalletByWalletId(String walletId) {
        if (walletId == null) throw new InvalidCredentialsException("Wallet Id cannot be null.");
        return walletRepo.findByWalletId(walletId);
    }

    public Wallet getWalletByUserId(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null.");
        return walletRepo.findByUserId(userId);
    }

    public double getBalanceByUserId(String userId, String pin) {
        Wallet wallet = getValidWalletByUserId(userId);
        if (validatePassword(pin, wallet.getWalletId())) {
            throw new InvalidCredentialsException("Invalid wallet PIN. Access denied.");
        }
        return wallet.getBalance();
    }

    public Wallet createSystemRevenueWallet() {
        Wallet revenueWallet = getRevenueWallet();
        if (revenueWallet == null) {
            Wallet newRevenue = new Wallet(REVENUE_WALLET_ID, SYSTEM_USER_ID, 0.0);
            walletRepo.save(newRevenue);
            return newRevenue;
        }
        return revenueWallet;
    }

    public Wallet createSystemEscrowWallet() {
        Wallet escrowWallet = getEscrowWallet();
        if (escrowWallet == null) {
            Wallet newEscrow = new Wallet(ESCROW_WALLET_ID, SYSTEM_USER_ID, 0.0);
            walletRepo.save(newEscrow);
            return newEscrow;
        }
        return escrowWallet;
    }

    public Wallet createWallet(String userId) {
        if (userId == null) throw new IllegalArgumentException("UserId cannot be null.");
        Wallet existingWallet = walletRepo.findByUserId(userId);
        if (existingWallet != null) {
            throw new IllegalStateException("Wallet already exists for this user.");
        }

        String walletId = IdGeneratorUtil.generate(IdPrefix.WAL);
        Wallet newWallet = new Wallet(walletId, userId, INITIAL_BALANCE);
        walletRepo.save(newWallet);

        try {
            transactionService.logDeposit(walletId, INITIAL_BALANCE, "Initial Sign-up Bonus");
        } catch (Exception e) {
            System.err.println("Warning: Failed to log initial bonus transaction.");
        }

        return newWallet;
    }

    public void creditAmountByWalletId(String pin, String walletId, double amount) {
        validateAmount(amount);
        if (validatePassword(pin, walletId)) {
            throw new InvalidCredentialsException("Invalid Wallet PIN. Deposit denied.");
        }

        Lock lock = getWalletLock(walletId);
        lock.lock();
        try {
            Wallet wallet = getValidWallet(walletId);
            wallet.credit(amount);
            walletRepo.update(wallet);

            try {
                transactionService.logDeposit(walletId, amount, "Deposit from Bank to Wallet");
            } catch (Exception e) {
                wallet.debit(amount);
                walletRepo.update(wallet);
                throw new RuntimeException("System error during deposit. Transaction rolled back.", e);
            }
        } finally {
            lock.unlock();
        }
    }

    public void withdrawAmountByWalletId(String pin, String walletId, double amount, boolean isPayment) {
        validateAmount(amount);

        Lock lock = getWalletLock(walletId);
        lock.lock();
        try {
            Wallet wallet = getValidWallet(walletId);

            if (validatePassword(pin, wallet.getWalletId())) {
                throw new InvalidCredentialsException("Invalid Wallet PIN. Withdrawal denied.");
            }

            if ((!isPayment) && (wallet.getBalance() - amount) < MIN_BALANCE_FOR_WITHDRAWAL) {
                throw new InsufficientFundsException("Minimum $1000 balance is requirement in wallet.");
            }

            if (isPayment && (wallet.getBalance() - amount) < 0) {
                throw new InsufficientFundsException("Insufficient amount in wallet.");
            }

            wallet.debit(amount);
            walletRepo.update(wallet);

            try {
                transactionService.logWithdrawal(walletId, amount, "Withdrawal from Wallet to Bank");
            } catch (Exception e) {
                wallet.credit(amount);
                walletRepo.update(wallet);
                throw new RuntimeException("System error during withdrawal. Money refunded to wallet.", e);
            }
        } finally {
            lock.unlock();
        }
    }

    public void executeSystemTransfer(String sourceWalletId, String destinationWalletId, double amount) {
        validateAmount(amount);

        String firstLockId = sourceWalletId.compareTo(destinationWalletId) < 0 ? sourceWalletId : destinationWalletId;
        String secondLockId = sourceWalletId.compareTo(destinationWalletId) < 0 ? destinationWalletId : sourceWalletId;

        Lock lock1 = getWalletLock(firstLockId);
        Lock lock2 = getWalletLock(secondLockId);

        lock1.lock();
        lock2.lock();
        try {
            Wallet source = getValidWallet(sourceWalletId);
            Wallet destination = getValidWallet(destinationWalletId);

            if (source.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient funds in source wallet for transfer.");
            }

            source.debit(amount);
            walletRepo.update(source);

            try {
                destination.credit(amount);
                walletRepo.update(destination);
            } catch (Exception e) {
                source.credit(amount);
                walletRepo.update(source);
                throw new RuntimeException("Transfer failed at destination. Source wallet refunded.", e);
            }

        } finally {
            lock2.unlock();
            lock1.unlock();
        }
    }

    private Wallet getValidWallet(String walletId) {
        Wallet wallet = walletRepo.findByWalletId(walletId);
        if (wallet == null) throw new ResourceNotFoundException("Wallet does not exist.");
        return wallet;
    }

    private Wallet getValidWalletByUserId(String userId) {
        Wallet wallet = walletRepo.findByUserId(userId);
        if (wallet == null) throw new ResourceNotFoundException("User does not have a wallet.");
        return wallet;
    }

    public Wallet getEscrowWallet() {
        return walletRepo.findByWalletId(ESCROW_WALLET_ID);
    }

    public Wallet getRevenueWallet() {
        return walletRepo.findByUserId(SYSTEM_USER_ID);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero.");
    }

    private boolean validatePassword(String pin, String walletId) {
        if ("SYSTEM-ESCROW".equals(walletId)) return false;
        return !walletCredentialService.verifyWalletPassword(walletId, pin);
    }
}