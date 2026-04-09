package rental.command;

import java.util.Scanner;

public interface RentalCommand {
    void execute(Scanner scanner, String customerId);
}
