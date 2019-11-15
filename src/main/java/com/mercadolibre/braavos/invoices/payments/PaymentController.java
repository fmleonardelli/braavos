package com.mercadolibre.braavos.invoices.payments;

import com.mercadolibre.braavos.invoices.InvoiceService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PaymentController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("payments/v1")
    public ResponseEntity create(@Valid @RequestBody PaymentInputApi payment) throws Throwable {
        val res = invoiceService.addPayment(payment);
        if (res.isRight()) return new ResponseEntity<>(HttpStatus.ACCEPTED);
        else throw res.getLeft();
    }

    @PostMapping("payments")
    public Single<ResponseEntity> createReactive(@Valid @RequestBody PaymentInputApi payment) throws Throwable {
        return invoiceService.addPaymentReactive(payment)
                .subscribeOn(Schedulers.io())
                .map(r -> new ResponseEntity(HttpStatus.ACCEPTED));
    }
}
