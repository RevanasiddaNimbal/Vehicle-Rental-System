package config;

import authentication.repository.OtpStorage;
import authentication.service.OtpService;
import notification.provider.BrevoEmailProvider;
import notification.service.EmailService;

public class NotificationConfig {

    private EmailService emailService;
    private OtpService otpService;

    public EmailService getEmailService() {
        if (emailService == null) {
            emailService = new BrevoEmailProvider(new EmailPropertiesConfig());
        }
        return emailService;
    }

    public OtpService getOtpService() {
        if (otpService == null) {
            otpService = new OtpService(new OtpStorage(), getEmailService());
        }
        return otpService;
    }
}