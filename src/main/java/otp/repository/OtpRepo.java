package otp.repository;

import otp.model.OtpData;

public interface OtpRepo {
    void save(OtpData otpData);

    OtpData findByEmail(String email);

    void deleteByEmail(String email);
}