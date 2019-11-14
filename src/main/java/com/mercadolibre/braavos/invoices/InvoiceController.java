package com.mercadolibre.braavos.invoices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.braavos.invoices.api.*;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
                                                            @RequestParam(required = false) String date_from,
                                                            @RequestParam(required = false) String date_to,
                                                            @RequestParam(required = false) Integer limit,
                                                            @RequestParam(required = false) Integer offset) throws Throwable {

        val parameters = new InvoiceParametersApi(Option.of(user_id), Option.of(date_from), Option.of(date_to) , Option.of(limit),  Option.of(offset));
        return convertToResponsePaginated(invoiceService.findInvoices(parameters));
    }

    @GetMapping("invoices/summary")
    public ResponseEntity<InvoicesSummaryApi> getSummary(@RequestParam String user_id,
                                                         @RequestParam(required = false) String date_from,
                                                         @RequestParam(required = false) String date_to) throws Throwable {

        val parameters = new InvoiceParametersForSummaryApi(user_id, Option.of(date_from), Option.of(date_to));
        val res = invoiceService.findInvoicesSummary(parameters);
        if (res.isRight()) {
            return new ResponseEntity<>(res.get(), HttpStatus.OK);
        } else {
            throw res.getLeft();
        }
    }
}
