package util;

import exception.ServiceException;

import java.security.SecureRandom;

public class IdGeneratorUtil {
    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final SecureRandom RAND = new SecureRandom();

    private static int rentalCounter = 1;
    private static int penaltyCounter = 1;
    private static int cancellationCounter = 1;
    private static int walletCounter = 1;

    public static int generateRentalId() {
        return rentalCounter++;
    }

    public static String generatePenaltyId() {
        return String.format("PEN-%03d", penaltyCounter++);
    }

    public static String generateCancellationId() {
        return String.format("CAN-%03d", cancellationCounter++);
    }

    public static String generateWalletId() {
        return String.format("WAL-%03d", walletCounter++);
    }

    public static String generate(IdPrefix prefix) {
        return generate(prefix.value(), 5);
    }

    public static String generate(String prefix, int randomLength) {
        if (prefix == null || prefix.isBlank()) throw new ServiceException("Prefix cannot be blank");
        if (randomLength < 3 || randomLength > 20)
            throw new ServiceException("randomLength must be between 6 and 20");

        StringBuilder sb = new StringBuilder();
        sb.append(prefix.toUpperCase()).append('-');

        for (int i = 0; i < randomLength; i++) {
            sb.append(BASE62[RAND.nextInt(BASE62.length)]);
        }
        return sb.toString();
    }
}

