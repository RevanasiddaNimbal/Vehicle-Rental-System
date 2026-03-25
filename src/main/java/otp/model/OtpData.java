package otp.model;

public class OtpData {
    private final String email;
    private final String code;
    private final long expiryTimeMillis;

    public OtpData(String email, String code, long expiryTimeMillis) {
        this.email = email;
        this.code = code;
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimeMillis;
    }
}