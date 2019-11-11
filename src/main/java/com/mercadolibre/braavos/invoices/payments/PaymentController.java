package com.mercadolibre.braavos.invoices.payments;

import com.mercadolibre.braavos.invoices.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("payments")
    public String create(@RequestBody PaymentApi payment) {
        invoiceService.addPayment(payment);
        return "ok";
    }
}
