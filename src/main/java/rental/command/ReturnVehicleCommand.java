package rental.command;

import UI.UserPrinter;
import exception.ResourceNotFoundException;
import penalty.model.Penalty;
import rental.facade.RentalFacade;
import rental.model.Rental;
import rental.service.RentalService;
import util.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReturnVehicleCommand implements RentalCommand {
    private final RentalFacade rentalFacade;
    private final RentalService rentalService;
    private final UserPrinter<Rental> rentalPrinter;
    private final UserPrinter<Penalty> penaltyPrinter;

    public ReturnVehicleCommand(RentalFacade rentalFacade, RentalService rentalService,
                                UserPrinter<Rental> rentalPrinter, UserPrinter<Penalty> penaltyPrinter) {
        this.rentalFacade = rentalFacade;
        this.rentalService = rentalService;
        this.rentalPrinter = rentalPrinter;
        this.penaltyPrinter = penaltyPrinter;
    }

    @Override
    public void execute(Scanner scanner, String customerId) {
        List<Rental> rentalsToReturn = collectRentalsToReturn(scanner, customerId);

        if (rentalsToReturn.isEmpty()) return;

        rentalPrinter.print(rentalsToReturn);
        List<Penalty> penalties = rentalFacade.calculateReturnPenalties(rentalsToReturn);

        if (!penalties.isEmpty()) {
            System.out.println("Penalties applied for return:");
            penaltyPrinter.print(penalties);
        }

        if (!InputUtil.readString(scanner, "Confirm return? (Y/N)").equalsIgnoreCase("Y")) {
            System.out.println("Return cancelled.");
            return;
        }

        try {
            rentalFacade.processReturn(customerId, rentalsToReturn, penalties);
            System.out.println("Vehicle(s) returned successfully! Refunds/Payouts are processing.");
        } catch (Exception e) {
            System.out.println("Failed to complete return: " + e.getMessage());
        }
    }

    private List<Rental> collectRentalsToReturn(Scanner input, String customerId) {
        List<Rental> rentalsToReturn = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {
            int rentalId = InputUtil.readPositiveInt(input, "Enter Rental ID to return");
            try {
                Rental rental = rentalService.processReturnValidation(rentalId, customerId);
                rentalsToReturn.add(rental);
                System.out.println("Rental added for return.");
            } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Validation Failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("System Error: Could not validate rental.");
            }

            String choice = InputUtil.readString(input, "Return another vehicle? (Y/N)");
            addMore = choice.equalsIgnoreCase("Y");
        }
        return rentalsToReturn;
    }
}