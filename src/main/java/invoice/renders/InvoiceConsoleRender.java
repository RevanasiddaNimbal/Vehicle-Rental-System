package invoice.renders;

import invoice.model.Invoice;
import rental.model.Rental;

public class InvoiceConsoleRender implements InvoiceRender {
    @Override
    public void render(Invoice invoice) {

        System.out.println("\n====================== RENTAL INVOICE =======================");

        System.out.println("Customer ID   : " + invoice.getCustomer().getId());
        System.out.println("Customer Name : " + invoice.getCustomer().getName());
        System.out.println("Customer Phone : " + invoice.getCustomer().getPhone());
        System.out.println("Customer Email : " + invoice.getCustomer().getEmail());


        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Vehicle ID | Days | Base Charge | Wkd Charge | Discount | Deposit  | Rent");
        System.out.println("-------------------------------------------------------------------------");

        for (Rental rental : invoice.getRentals()) {
            System.out.printf("%-10s | %-4d | %-11.2f | %-10.2f | %-8.2f | %-8.2f | %-10.2f%n",
                    rental.getVehicleId(),
                    rental.getDays(),
                    rental.getBasePrice(),
                    rental.getWeekendCharge(),
                    rental.getDiscount(),
                    rental.getSecurityDeposit(),
                    rental.getTotalPrice());
        }

        System.out.println("-------------------------------------------------------------------------");


        System.out.printf("%-10s | %-4s| %-11.2f | %-10.2f | %-8.2f | %-8.2f | %-10.2f%n",
                "TOTAL",
                " - ",
                invoice.getTotalBasePrice(),
                invoice.getTotalWeekendPrice(),
                invoice.getTotalDiscountPrice(),
                invoice.getTotalSecurityDeposit(),
                invoice.getTotalNetPrice());

        System.out.println("-------------------------------------------------------------------------");

        System.out.printf("Total Rental Fare   : %.2f%n", invoice.getTotalNetPrice());
        System.out.printf("Security Deposit : %.2f%n", invoice.getTotalSecurityDeposit());
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("TOTAL NET AMOUNT  : %.2f%n", invoice.getGrandTotalPayable());
        System.out.println("=========================================================================");
    }
}