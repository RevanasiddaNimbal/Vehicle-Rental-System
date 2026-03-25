package initializer;

import admin.service.AdminService;
import wallet.model.Wallet;
import wallet.service.WalletCredentialService;
import wallet.service.WalletService;

public class SystemInitializer {

    private final AdminService adminService;
    private final WalletService walletService;
    private final WalletCredentialService credentialService;

    public SystemInitializer(AdminService adminService,
                             WalletService walletService,
                             WalletCredentialService credentialService) {
        this.adminService = adminService;
        this.walletService = walletService;
        this.credentialService = credentialService;
    }

    public void initialize() {
        try {
            System.out.println("Initializing default admins... ");
            adminService.createDefaultAdmins();

            System.out.println("Initializing system wallet... ");
            Wallet wallet = walletService.createSystemWallet();

            if (wallet == null) {
                throw new IllegalStateException("System wallet creation returned null.");
            }

            System.out.println("Initializing default wallet credentials... ");

            credentialService.defaultWalletCredential(wallet.getWalletId());

            System.out.println("System Initialized Successfully.\n");

        } catch (Exception e) {
            System.err.println("Reason: " + e.getMessage());
        }
    }
}