package com.mercadolibre.braavos.invoices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.braavos.invoices.api.InvoiceApi;
import com.mercadolibre.braavos.invoices.api.InvoiceParametersApi;
import com.mercadolibre.braavos.invoices.api.Paginated;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceController implements Controller<InvoiceApi> {

    Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("invoices")
    public ResponseEntity<Paginated<InvoiceApi>> getCharges(@RequestParam(required = false) String user_id,
                                                            @RequestParam(required = false) String period_date,
                                                            @RequestParam(required = false) Integer limit,
                                                            @RequestParam(required = false) Integer offset) throws Throwable {

        val parameters = new InvoiceParametersApi(Option.of(user_id), Option.of(period_date), Option.of(limit),  Option.of(offset));
        return convertToResponsePaginated(invoiceService.findInvoices(parameters));
    }
}
