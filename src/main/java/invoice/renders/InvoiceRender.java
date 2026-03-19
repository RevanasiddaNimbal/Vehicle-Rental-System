package invoice.renders;

import invoice.model.Invoice;

public interface InvoiceRender {
    void render(Invoice invoice);
}
