package otp.service;

import exception.OtpVerificationException;
import notification.model.EmailMessage;
import notification.service.EmailService;
import otp.model.OtpData;
import otp.repository.OtpRepo;
import util.OtpUtil;

public class OtpService {
    private final OtpRepo repository;
    private final EmailService mailService;

    public OtpService(OtpRepo repository, EmailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    public void sendOtp(String email) {
        String code = OtpUtil.generateOtp(6);
        long expiryTime = System.currentTimeMillis() + 5 * 60 * 1000;

        OtpData otpData = new OtpData(email, code, expiryTime);
        repository.save(otpData);

        String body = "You have requested an action in the Vehicle Rental system.\n" +
                "Your OTP is: " + code + "\n" +
                "This code will expire in 5 minutes.";

        EmailMessage message = new EmailMessage(email, "OTP Verification", body);

        if (!mailService.sendEmail(message)) {
            throw new OtpVerificationException("Failed to send OTP email.");
        }
    }

    public void verifyOtp(String email, String code) {
        OtpData otpData = repository.findByEmail(email);

        if (otpData == null) {
            throw new OtpVerificationException("No OTP found for this email. Please request a new one.");
        }

        if (otpData.isExpired()) {
            repository.deleteByEmail(email);
            throw new OtpVerificationException("OTP has expired. Please request a new one.");
        }

        if (otpData.getCode().equals(code)) {
            repository.deleteByEmail(email);
            return;
        }

        repository.deleteByEmail(email);
        throw new OtpVerificationException("Incorrect OTP. For security, please request a new one.");
    }
}