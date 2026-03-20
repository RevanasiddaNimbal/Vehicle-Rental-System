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
        List<Penalty> penalties = penaltyService.getAllPenalty();
        if (penalties.isEmpty()) {
            System.out.println("No penalties found");
            return;
        }
        printer.print(penalties);
    }

    public void viewPenaltiesByCustomerId(String customerId) {
        List<Penalty> penalties = penaltyService.getPenaltiesByCustomerId(customerId);
        if (penalties.isEmpty()) {
            System.out.println("No penalties found");
            return;
        }
        printer.print(penalties);
    }


}
