package penalty.controller;

import UI.UserPrinter;
import penalty.model.Penalty;
import penalty.service.PenaltyService;

import java.util.List;

public class PenaltyController {
    private final PenaltyService penaltyService;
    private final UserPrinter<Penalty> printer;

    public PenaltyController(PenaltyService penaltyService, UserPrinter<Penalty> printer) {
        this.penaltyService = penaltyService;
        this.printer = printer;
    }

    public void viewPenalties() {
        try {
            List<Penalty> penalties = penaltyService.getAllPenalties();
            if (penalties.isEmpty()) {
                System.out.println("No penalties found.");
                return;
            }
            printer.print(penalties);
        } catch (Exception e) {
            System.out.println("Failed to fetch penalties: " + e.getMessage());
        }
    }

    public void viewPenaltiesByCustomerId(String customerId) {
        try {
            List<Penalty> penalties = penaltyService.getPenaltiesByCustomerId(customerId);
            if (penalties.isEmpty()) {
                System.out.println("No penalties found for this customer.");
                return;
            }
            printer.print(penalties);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to fetch customer penalties: " + e.getMessage());
        }
    }
}