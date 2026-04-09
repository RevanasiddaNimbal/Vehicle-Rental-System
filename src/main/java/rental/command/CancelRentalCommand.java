package rental.command;

import UI.UserPrinter;
import exception.ResourceNotFoundException;
import rental.facade.RentalFacade;
import rental.model.Rental;
import rental.service.RentalService;
import util.InputUtil;

import java.util.ArrayList;
import java.util.List;

public class CancelRentalCommand implements RentalCommand {

    private final RentalFacade rentalFacade;
    private final RentalService rentalService;
    private final UserPrinter<Rental> rentalPrinter;

    public CancelRentalCommand(RentalFacade rentalFacade,
                               RentalService rentalService,
                               UserPrinter<Rental> rentalPrinter) {
        this.rentalFacade = rentalFacade;
        this.rentalService = rentalService;
        this.rentalPrinter = rentalPrinter;
    }

    @Override
    public void execute(java.util.Scanner scanner, String customerId) {

        List<Rental> rentalsToCancel = collectRentalsToCancel(scanner, customerId);

        if (rentalsToCancel.isEmpty()) {
            System.out.println("No rentals to cancel.");
            return;
        }
        rentalPrinter.print(rentalsToCancel);

        String choice = InputUtil.readString(scanner, "Confirm cancellation? (Y/N)");
        if (!choice.equalsIgnoreCase("Y")) {
            System.out.println("Cancellation aborted.");
            return;
        }
        System.out.println("Cancellation process completed. Refunds are being processed if applicable.");

        for (Rental rental : rentalsToCancel) {
            try {
                double refundAmount = rentalFacade.processCancellation(customerId, rental);
            } catch (ResourceNotFoundException e) {
                System.out.println("Rental ID " + rental.getId() + " not found: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Failed to cancel rental ID " + rental.getId() + ". Reason: " + e.getMessage());
            }
        }
    }

    private List<Rental> collectRentalsToCancel(java.util.Scanner scanner, String customerId) {
        List<Rental> rentalsToCancel = new ArrayList<>();
        boolean addMore = true;

        while (addMore) {
            int rentalId = InputUtil.readPositiveInt(scanner, "Enter Rental ID to cancel");

            try {
                Rental rental = rentalService.processCancelValidation(rentalId, customerId);
                rentalsToCancel.add(rental);
                System.out.println("Rental added for cancellation.");
            } catch (ResourceNotFoundException | IllegalArgumentException | IllegalStateException e) {
                System.out.println("Validation Failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("System Error: Could not validate cancellation.");
            }

            String more = InputUtil.readString(scanner, "Cancel another rental? (Y/N)");
            addMore = more.equalsIgnoreCase("Y");
        }

        return rentalsToCancel;
    }
}