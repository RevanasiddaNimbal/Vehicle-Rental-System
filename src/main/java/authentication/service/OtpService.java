package authentication.service;

import authentication.repository.OtpStorage;
import notification.model.EmailMessage;
import notification.service.EmailService;
import util.OtpUtil;

public class OtpService {
    private final OtpStorage storage;
    private final EmailService mailService;

    public OtpService(OtpStorage storage, EmailService mailService) {
        this.storage = storage;
        this.mailService = mailService;
    }

    public void sendOtp(String email) {
        String code = OtpUtil.generateOtp(6);
        storage.storeOtp(email, code);
        String body = ("You have successfully registered in VehicleRental system.\n" +
                "Your OTP is: " + code + "\n" +
                "Please enter this OTP in the console to complete your registration.");
        EmailMessage message = new EmailMessage(
                email,
                "OTP Verification - VehicleRental",
                body
        );
        
        if (mailService.sendEmail(message)) {
            System.out.println(" OTP has been sent to email successfully.");
        }

    }

    public boolean verifyOtp(String email, String code) {
        String storedOtp = storage.getOtp(email);

        if (storedOtp.equals(code)) {
            storage.removeOtp(email);
            System.out.println("OTP verified successfully.");
            return true;
        } else {
            System.out.println("Incorrect OTP.");
            return false;
        }
    }
}
