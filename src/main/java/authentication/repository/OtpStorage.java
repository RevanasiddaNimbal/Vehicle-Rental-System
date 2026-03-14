package authentication.repository;

import java.util.HashMap;
import java.util.Map;

public class OtpStorage {
    private final Map<String, String> Storage = new HashMap<>();

    public void storeOtp(String email, String code) {
        Storage.put(email, code);
    }

    public String getOtp(String email) {
        return Storage.get(email);
    }

    public void removeOtp(String email) {
        Storage.remove(email);
    }
}
