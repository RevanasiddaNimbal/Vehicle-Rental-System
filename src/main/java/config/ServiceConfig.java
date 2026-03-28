package config;

import admin.service.AdminService;
import cancellation.service.CancellationService;
import customer.service.CustomerService;
import invoice.renders.InvoiceConsoleRender;
import invoice.renders.InvoiceRender;
import invoice.service.InvoiceService;
import payment.facade.PaymentFacade;
import payment.factory.PaymentStrategyFactory;
import penalty.factory.PenaltyStrategyFactory;
import penalty.service.PenaltyService;
import rental.billing.RentalPriceCalculator;
import rental.billing.RentalTimeCalculator;
import rental.scheduler.ReservationTimeoutManager;
import rental.service.RentalService;
import rental.stretegy.*;
import transaction.service.TransactionService;
import vehicle.service.VehicleService;
import vehicleowner.service.VehicleOwnerService;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig {

    private final RepositoryConfig repositoryConfig;

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

    public ServiceConfig(RepositoryConfig repositoryConfig) {
        this.repositoryConfig = repositoryConfig;
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
            paymentStrategyFactory = new PaymentStrategyFactory(getWalletService(), getTransactionService());
        }
        return paymentStrategyFactory;
    }

    public PaymentFacade getPaymentFacade() {
        if (paymentFacade == null) {
            paymentFacade = new PaymentFacade(getWalletService(), getTransactionService(), getVehicleService()
            );
        }
        return paymentFacade;
    }

    public CancellationService getCancellationService() {
        if (cancellationService == null) {
            cancellationService = new CancellationService(
                    getRentalService(),
                    getVehicleService(),
                    repositoryConfig.getCancellationRepo()
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

    public void shutdownBackgroundTasks() {
        if (rentalService != null) {
            rentalService.shutdownAsyncExecutor();
        }
        if (reservationTimeoutManager != null) {
            reservationTimeoutManager.shutdown();
        }
    }
}