package otp.repository;

import otp.model.OtpData;

import java.util.HashMap;
import java.util.Map;

public class OtpMemoryRepo implements OtpRepo {
    private final Map<String, OtpData> storage = new HashMap<>();

    @Override
    public void save(OtpData otpData) {
        storage.put(otpData.getEmail(), otpData);
    }

    @Override
    public OtpData findByEmail(String email) {
        return storage.get(email);
    }

    @Override
    public void deleteByEmail(String email) {
        storage.remove(email);
    }
}