package util;

import java.util.Scanner;

public class EnumUtil {
    // Generic function to handle the enum selection.
    public static <T extends Enum<T>> T selectEnum(Scanner input, Class<T> enumClass, String title) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class cannot be null");
        }
        T[] values = enumClass.getEnumConstants();
        return getChoice(input, values, title);
    }

    public static <T extends Enum<T>> T selectCategory(Scanner input, T[] values, String title) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("allowedValues cannot be null/empty");
        }
        return getChoice(input, values, title);
    }

    private static <T extends Enum<T>> T getChoice(Scanner input, T[] values, String title) {
        while (true) {
            System.out.println("\n----- " + title + " -------");
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

