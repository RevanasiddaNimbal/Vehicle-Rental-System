package util;

import java.util.Scanner;

public class InputUtil {
    private InputUtil() {
    }

    public static double readDouble(Scanner input, String message) {
        while (true) {
            System.out.print(message + ": ");
            String value = input.nextLine();
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
            String value = input.nextLine();
            try {
                return value.trim();
            } catch (Exception e) {
                System.out.println("Invalid Input!.Please try again");
            }
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
}
