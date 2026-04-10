package config;

import admin.service.AdminService;
import cancellation.factory.CancellationStrategyFactory;
import cancellation.service.CancellationService;
import customer.service.CustomerService;
import invoice.renders.InvoiceConsoleRender;
import invoice.renders.InvoiceRender;
import invoice.service.InvoiceService;
import notification.provider.BreveEmailProvider;
import notification.service.EmailService;
import otp.service.OtpService;
import payment.facade.PaymentFacade;
import payment.factory.PaymentStrategyFactory;
import penalty.factory.PenaltyStrategyFactory;
import penalty.service.PenaltyService;
import rental.billing.RentalPriceCalculator;
import rental.billing.RentalTimeCalculator;
import rental.facade.RentalFacade;
import rental.scheduler.ReservationTimeoutManager;
import rental.service.RentalService;
import rental.strategy.*;
import transaction.service.TransactionService;
import user.factory.UserResolverFactory;
import vehicle.service.VehicleService;
import vehicleowner.service.VehicleOwnerService;
import wallet.service.WalletCredentialService;
import wallet.service.WalletPinRecoveryService;
import wallet.service.WalletService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServiceConfig {

    private final RepositoryConfig repositoryConfig;
    private final ExecutorService paymentExecutorService;

    private StrategyConfig strategyConfig;
    private AdminService adminService;
    private VehicleService vehicleService;
    private VehicleOwnerService vehicleOwnerService;
    private CustomerService customerService;
    private InvoiceService invoiceService;
    private RentalTimeCalculator rentalTimeCalculator;
    private RentalPriceCalculator rentalPriceCalculator;
    private PenaltyStrategyFactory penaltyStrategyFactory;
    private PenaltyService penaltyService;
    private WalletCredentialService walletCredentialService;
    private WalletService walletService;
    private RentalService rentalService;
    private CancellationService cancellationService;
    private TransactionService transactionService;
    private PaymentStrategyFactory paymentStrategyFactory;
    private PaymentFacade paymentFacade;
    private SecurityDepositStrategy depositStrategy;
    private ReservationTimeoutManager reservationTimeoutManager;
    private OtpService otpService;
    private EmailService emailService;
    private WalletPinRecoveryService pinRecoveryService;
    private UserResolverFactory resolverFactory;
    private CancellationStrategyFactory cancelStrategyFactory;
    private RentalFacade rentalFacade;

    public ServiceConfig(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
        this.paymentExecutorService = Executors.newFixedThreadPool(5);
    }

    public AdminService getAdminService() {
        if (adminService == null) {
            adminService = new AdminService(repositoryConfig.getAdminRepo());
        }
        return adminService;
    }

    public VehicleService getVehicleService() {
        if (vehicleService == null) {
            vehicleService = new VehicleService(repositoryConfig.getVehicleRepo());
        }
        return vehicleService;
    }

    public VehicleOwnerService getVehicleOwnerService() {
        if (vehicleOwnerService == null) {
            vehicleOwnerService = new VehicleOwnerService(repositoryConfig.getVehicleOwnerRepo());
        }
        return vehicleOwnerService;
    }

    public CustomerService getCustomerService() {
        if (customerService == null) {
            customerService = new CustomerService(repositoryConfig.getCustomerRepo());
        }
        return customerService;
    }

    public InvoiceService getInvoiceService() {
        if (invoiceService == null) {
            InvoiceRender invoiceRender = new InvoiceConsoleRender();
            invoiceService = new InvoiceService(invoiceRender);
        }
        return invoiceService;
    }

    public RentalTimeCalculator getRentalTimeCalculator() {
        if (rentalTimeCalculator == null) {
            rentalTimeCalculator = new RentalTimeCalculator();
        }
        return rentalTimeCalculator;
    }

    public SecurityDepositStrategy getSecurityDepositStrategy() {
        if (depositStrategy == null) {
            depositStrategy = new DynamicDepositStrategy();
        }
        return depositStrategy;
    }

    public RentalPriceCalculator getRentalPriceCalculator() {
        if (rentalPriceCalculator == null) {
            Map<Integer, PricingStrategy> pricingStrategies = new HashMap<>();
            pricingStrategies.put(1, new BasePriceStrategy());
            pricingStrategies.put(2, new WeekendStrategy());
            pricingStrategies.put(3, new DiscountStrategy());
            rentalPriceCalculator = new RentalPriceCalculator(pricingStrategies, getSecurityDepositStrategy());
        }
        return rentalPriceCalculator;
    }

    public PenaltyStrategyFactory getPenaltyStrategyFactory() {
        if (penaltyStrategyFactory == null) {
            penaltyStrategyFactory = new PenaltyStrategyFactory(getRentalTimeCalculator());
        }
        return penaltyStrategyFactory;
    }

    public PenaltyService getPenaltyService() {
        if (penaltyService == null) {
            penaltyService = new PenaltyService(repositoryConfig.getPenaltyRepo(), getPenaltyStrategyFactory());
        }
        return penaltyService;
    }

    public WalletCredentialService getWalletCredentialService() {
        if (walletCredentialService == null) {
            walletCredentialService = new WalletCredentialService(repositoryConfig.getWalletCredentialRepo());
        }
        return walletCredentialService;
    }

    public TransactionService getTransactionService() {
        if (transactionService == null) {
            transactionService = new TransactionService(repositoryConfig.getTransactionRepo());
        }
        return transactionService;
    }

    public WalletService getWalletService() {
        if (walletService == null) {
            walletService = new WalletService(repositoryConfig.getWalletRepo(), getWalletCredentialService(), getTransactionService());
        }
        return walletService;
    }

    public RentalService getRentalService() {
        if (rentalService == null) {
            rentalService = new RentalService(
                    repositoryConfig.getRentalRepo(),
                    getRentalPriceCalculator(),
                    getVehicleService(),
                    getVehicleOwnerService(),
                    getCustomerService(),
                    getRentalTimeCalculator()
            );
        }
        return rentalService;
    }

    public PaymentStrategyFactory getPaymentStrategyFactory() {
        if (paymentStrategyFactory == null) {
            paymentStrategyFactory = new PaymentStrategyFactory(getWalletService(), getTransactionService(), paymentExecutorService);
        }
        return paymentStrategyFactory;
    }

    public PaymentFacade getPaymentFacade() {
        if (paymentFacade == null) {
            paymentFacade = new PaymentFacade(getWalletService(), getTransactionService(), getVehicleService());
        }
        return paymentFacade;
    }

    public CancellationStrategyFactory getCancellationStrategyFactory() {
        if (cancelStrategyFactory == null) {
            cancelStrategyFactory = new CancellationStrategyFactory(strategyConfig.getCancellationStrategies());
        }
        return cancelStrategyFactory;
    }

    public CancellationService getCancellationService() {
        if (cancellationService == null) {
            cancellationService = new CancellationService(
                    getRentalService(),
                    getVehicleService(),
                    repositoryConfig.getCancellationRepo(),
                    getPaymentFacade(),
                    getCancellationStrategyFactory()
            );
        }
        return cancellationService;
    }

    public ReservationTimeoutManager getReservationTimeoutManager() {
        if (reservationTimeoutManager == null) {
            reservationTimeoutManager = new ReservationTimeoutManager(getVehicleService());
        }
        return reservationTimeoutManager;
    }

    public StrategyConfig getStrategyConfig(Scanner input) {
        if (strategyConfig == null) {
            strategyConfig = new StrategyConfig(input, getWalletService(), getWalletCredentialService(), getCustomerService(), getVehicleOwnerService(), getAdminService(), getOtpService());
        }
        return strategyConfig;
    }

    public EmailService getEmailService() {
        if (emailService == null) {
            emailService = new BreveEmailProvider(new EmailPropertiesConfig());
        }
        return emailService;
    }

    public OtpService getOtpService() {
        if (otpService == null) {
            otpService = new OtpService(repositoryConfig.getOtpRepo(), getEmailService());
        }
        return otpService;
    }

    public UserResolverFactory getResolverFactory(Scanner input) {
        if (resolverFactory == null) {
            resolverFactory = new UserResolverFactory(getStrategyConfig(input).getResolverRegistries());
        }
        return resolverFactory;
    }

    public WalletPinRecoveryService getPinRecoveryService(Scanner input) {
        if (pinRecoveryService == null) {
            pinRecoveryService = new WalletPinRecoveryService(getWalletService(), getWalletCredentialService(), getOtpService(), getResolverFactory(input));
        }
        return pinRecoveryService;
    }

    public RentalFacade getRentalFacade() {
        if (rentalFacade == null) {
            rentalFacade = new RentalFacade(
                    getRentalService(),
                    getVehicleService(),
                    getPaymentFacade(),
                    getPenaltyService(),
                    getCancellationService(),
                    getReservationTimeoutManager()
            );
        }
        return rentalFacade;
    }

    public void shutdownBackgroundTasks() {
        if (rentalService != null) {
            rentalService.shutdownAsyncExecutor();
        }
        if (reservationTimeoutManager != null) {
            reservationTimeoutManager.shutdown();
        }
        if (otpService != null) {
            otpService.shutdown();
        }

        paymentExecutorService.shutdown();
        try {
            if (!paymentExecutorService.awaitTermination(3, TimeUnit.SECONDS)) {
                paymentExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            paymentExecutorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}