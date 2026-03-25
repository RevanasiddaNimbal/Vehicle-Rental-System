package config;

import notification.provider.BrevoEmailProvider;
import notification.service.EmailService;
import otp.repository.OtpPostgresRepo;
import otp.service.OtpService;

public class NotificationConfig {
    private DatabaseConfig databaseConfig;
    private EmailService emailService;
    private OtpService otpService;

    public NotificationConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    public EmailService getEmailService() {
        if (emailService == null) {
            emailService = new BrevoEmailProvider(new EmailPropertiesConfig());
        }
        return emailService;
    }

    public OtpService getOtpService() {
        if (otpService == null) {
            otpService = new OtpService(new OtpPostgresRepo(databaseConfig.getPostgresConnection()), getEmailService());
        }
        return otpService;
    }
}