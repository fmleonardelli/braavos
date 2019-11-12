package com.mercadolibre.braavos.invoices.payments;

import com.mercadolibre.braavos.invoices.repo.InvoiceRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentService {

    @Autowired
    InvoiceRepository invoiceRepository;


}
