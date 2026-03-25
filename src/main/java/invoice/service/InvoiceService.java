package invoice.service;

import invoice.model.Invoice;
import invoice.renders.InvoiceRender;

public class InvoiceService {
    private final InvoiceRender invoiceRender;

    public InvoiceService(InvoiceRender invoiceRender) {
        this.invoiceRender = invoiceRender;
    }

    public void printInvoice(Invoice invoice) {
        if (invoice == null || invoice.getCustomer() == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        invoiceRender.render(invoice);
    }
}