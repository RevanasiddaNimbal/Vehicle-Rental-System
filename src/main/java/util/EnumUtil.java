package util;

import java.util.Scanner;

public class EnumUtil {
    // Generic function to handle the enum selection.
    public static <T extends Enum<T>> T selectEnum(Scanner input, Class<T> enumClass, String title) {
        while (true) {
            T[] values = enumClass.getEnumConstants();

            System.out.println("\n----- " + title + " -----");
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + "." + values[i].name());
            }
            int choice = InputUtil.readPositiveInt(input, "Enter your choice");

            if (choice < 1 || choice > values.length) {
                System.out.println("Invalid choice.Please try again.");
                continue;
            }
            return values[choice - 1];
        }
    }
}

