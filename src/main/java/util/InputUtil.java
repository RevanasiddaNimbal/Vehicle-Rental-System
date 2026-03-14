package util;

import java.util.Scanner;

public class InputUtil {
    private InputUtil() {
    }

    public static double readDouble(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Invalid input. Please try again");
                continue;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input!.Please try again");
            }
        }
    }

    public static String readString(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Please Enter Valid Value!");
                continue;
            }
            if (value.length() > 25) {
                System.out.println("Please Enter with in 25 Characters!");
                continue;
            }
            return value;
        }
    }

    public static int readPositiveInt(Scanner in, String message) {
        while (true) {
            System.out.print(message + ": ");
            String input = in.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid input!");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Please enter correct value!.");
                    continue;
                }
                return value;
            } catch (Exception e) {
                System.out.println("Invalid input!. Please try again.");
            }
        }
    }

    public static String readValidEmail(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String email = input.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Invalid input!");
                continue;
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                System.out.println("Invalid email format! Please try again.");
                continue;
            }

            return email;
        }
    }

    public static String readValidPhone(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String phone = input.nextLine().trim();

            if (phone.isEmpty()) {
                System.out.println("Invalid input!");
                continue;
            }

            if (!phone.matches("^[6-9]\\d{9}$")) {
                System.out.println("Invalid phone number! Enter a valid 10-digit number.");
                continue;
            }
            return phone;
        }
    }

    public static String readValidPassword(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String value = input.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Please Enter Valid Value!");
                continue;
            }
            if (value.length() < 6) {
                System.out.println("Password should be at least 6 characters!");
                continue;
            }
            return value;
        }
    }
}
