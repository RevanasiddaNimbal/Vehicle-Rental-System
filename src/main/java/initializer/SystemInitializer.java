package initializer;

import config.ServiceConfig;
import wallet.model.Wallet;

public class SystemInitializer {

    private final ServiceConfig serviceConfig;
    private boolean isInitialized = false;

    public SystemInitializer(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public void initialize() {
        if (isInitialized) return;

        try {
            System.out.println("Initializing default admins... ");
            serviceConfig.getAdminService().createDefaultAdmins();

            System.out.println("Initializing system wallet... ");
            Wallet wallet = serviceConfig.getWalletService().createSystemRevenueWallet();

            Wallet escarowWallet = serviceConfig.getWalletService().createSystemEscrowWallet();
            if (wallet == null) {
                throw new IllegalStateException("System wallet creation returned null.");
            }
            if (escarowWallet == null) {
                throw new IllegalStateException("System escrow wallet creation returned null.");
            }

            System.out.println("Initializing default wallet credentials... ");
            serviceConfig.getWalletCredentialService().defaultWalletCredential(wallet.getWalletId());

            System.out.println("System Initialized Successfully.\n");

            isInitialized = true;

        } catch (Exception e) {
            System.err.println("Reason: " + e.getMessage());
        }
    }
}