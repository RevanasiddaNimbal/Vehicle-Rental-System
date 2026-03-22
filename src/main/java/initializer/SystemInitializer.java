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
        adminService.createDefaultAdmins();
        System.out.println("default admins creation checked...");
        Wallet wallet = walletService.createSystemWallet();
        System.out.println("default wallet creation checked...");
        credentialService.defaultWalletCredential(wallet.getWalletId());
        System.out.println("default wallet credential creation checked...");

        System.out.println("System initialized successfully");
    }
}