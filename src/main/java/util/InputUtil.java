package util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static double readDouble(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Invalid input. Please try again");
                attempts++;
                continue;
            }
            try {
                double doubleValue = Double.parseDouble(value);
                if (doubleValue < 0) {
                    System.out.println("Value cannot be negative. Try again.");
                    attempts++;
                    continue;
                }
                return doubleValue;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input!.Please try again");
                attempts++;
            }
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static String readString(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Please Enter Valid Value!");
                attempts++;
                continue;
            }
            if (value.length() > 25) {
                System.out.println("Please Enter with in 25 Characters!");
                attempts++;
                continue;
            }
            return value;
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static int readPositiveInt(Scanner in, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String input = in.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid input!");
                attempts++;
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Please enter correct value!.");
                    attempts++;
                    continue;
                }
                return value;
            } catch (Exception e) {
                System.out.println("Invalid input!. Please try again.");
                attempts++;
            }
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static String readValidEmail(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String email = input.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Invalid input!");
                attempts++;
                continue;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Invalid email format! Please try again.");
                attempts++;
                continue;
            }

            return email;
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static String readValidPhone(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String phone = input.nextLine().trim();

            if (phone.isEmpty()) {
                System.out.println("Invalid input!");
                attempts++;
                continue;
            }

            if (!phone.matches("^[6-9]\\d{9}$")) {
                System.out.println("Invalid phone number! Enter a valid 10-digit number.");
                attempts++;
                continue;
            }
            return phone;
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static String readValidPassword(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Please Enter Valid Value!");
                attempts++;
                continue;
            }
            if (value.length() < 6) {
                System.out.println("Password should be at least 6 characters!");
                attempts++;
                continue;
            }
            return value;
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static LocalDate readValidDate(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("Invalid input. Please try again.");
                attempts++;
                continue;
            }

            try {
                LocalDate date = LocalDate.parse(value, DATE_FORMAT);
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Invalid date! Please try again.");
                    attempts++;
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date.Please try again.");
                attempts++;
            }
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }

    public static LocalTime readValidTime(Scanner input, String message) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("Invalid input. Please try again.");
                attempts++;
                continue;
            }

            try {
                return LocalTime.parse(value, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time. Please try again.");
                attempts++;
            }
        }
        throw new RuntimeException("Maximum attempts reached. Operation aborted.");
    }
}