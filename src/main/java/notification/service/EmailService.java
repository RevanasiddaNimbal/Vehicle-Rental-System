package notification.service;

import notification.model.EmailMessage;

public interface EmailService {
    boolean sendEmail(EmailMessage message);
}
