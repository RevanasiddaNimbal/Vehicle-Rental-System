package cancellation.controller;

import UI.UserPrinter;
import cancellation.model.CancellationRecord;
import cancellation.service.CancellationService;

import java.util.List;

public class CancellationController {
    private final CancellationService cancellationService;
    private final UserPrinter<CancellationRecord> printer;

    public CancellationController(CancellationService cancellationService, UserPrinter<CancellationRecord> printer) {
        this.cancellationService = cancellationService;
        this.printer = printer;
    }

    public void viewCancellations() {
        List<CancellationRecord> records = cancellationService.getAllCancellationRecords();
        if (records.isEmpty()) {
            System.out.println("There are no cancellations found.");
            return;
        }
        printer.print(records);
    }

    public void viewCancellationsByCustomerId(String customerId) {
        List<CancellationRecord> records = cancellationService.getAllCancellationRecordsByCustomerId(customerId);
        if (records.isEmpty()) {
            System.out.println("There are no cancellations for this Customer.");
            return;
        }
        printer.print(records);
    }

    public void viewCancellationsByOwnerId(String ownerId) {
        List<CancellationRecord> records = cancellationService.getAllCancellationRecordsByOwnerId(ownerId);
        if (records.isEmpty()) {
            System.out.println("There are no cancellations for this Owner.");
            return;
        }
        printer.print(records);

    }
}
