package invoice.renders;

import invoice.model.Invoice;
import rental.model.Rental;

public class InvoiceConsoleRender implements InvoiceRender {
    @Override
    public void render(Invoice invoice) {

        System.out.println("\n================ RENTAL INVOICE =================");

        System.out.println("Customer ID   : " + invoice.getCustomer().getId());
        System.out.println("Customer Name : " + invoice.getCustomer().getName());
        System.out.println("Customer Phone : " + invoice.getCustomer().getPhone());
        System.out.println("Customer Email : " + invoice.getCustomer().getEmail());

        System.out.println("-------------------------------------------------------------");
        System.out.println("Vehicle ID | Days | Base Charge | Weekend Charge | Discount | Total");
        System.out.println("-------------------------------------------------------------");

        for (Rental rental : invoice.getRentals()) {
            System.out.printf("%-10s | %-4d | %-11.2f | %-14.2f | %-8.2f | %-10.2f%n",
                    rental.getVehicleId(),
                    rental.getDays(),
                    rental.getBasePrice(),
                    rental.getWeekendCharge(),
                    rental.getDiscount(),
                    rental.getTotalPrice());
        }


        System.out.println("-------------------------------------------------------------");

        System.out.printf("%-10s | %-4s| %-11.2f | %-14.2f | %-8.2f | %-10.2f%n",
                "TOTAL",
                " - ",
                invoice.getTotalBasePrice(),
                invoice.getTotalWeekendPrice(),
                invoice.getTotalDiscountPrice(),
                invoice.getTotalNetPrice());

        System.out.println("-------------------------------------------------------------");
        System.out.printf("Net Total Amount : %.2f%n ", invoice.getTotalNetPrice());
        System.out.println("=============================================================");
    }
}
