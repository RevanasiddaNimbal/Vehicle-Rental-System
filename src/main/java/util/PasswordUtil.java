package util;

public class PasswordUtil {
    public static String getHashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    public static boolean verify(String input, String storedHash) {
        return getHashPassword(input).equals(storedHash);
    }
}
