package invoice.service;

import invoice.model.Invoice;
import invoice.renders.InvoiceRender;

public class InvoiceService {
    private final InvoiceRender invoiceRender;

    public InvoiceService(InvoiceRender invoiceRender) {
        this.invoiceRender = invoiceRender;
    }

    public void printInvoice(Invoice invoice) {
        invoice.calculateTotalRents();
        invoiceRender.render(invoice);
    }
}
