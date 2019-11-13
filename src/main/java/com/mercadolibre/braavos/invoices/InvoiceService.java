package com.mercadolibre.braavos.invoices;

import com.mercadolibre.braavos.invoices.api.InvoiceParametersApi;
import com.mercadolibre.braavos.invoices.api.error.DataNotFound;
import com.mercadolibre.braavos.invoices.api.InvoiceApi;
import com.mercadolibre.braavos.invoices.api.Paginated;
import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.charges.ChargeState;
import com.mercadolibre.braavos.invoices.kafka.EventNotification;
import com.mercadolibre.braavos.invoices.payments.PaymentInputApi;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import com.mercadolibre.braavos.invoices.repo.InvoiceParametersRepository;
import com.mercadolibre.braavos.invoices.repo.InvoiceRepository;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceService {

    @Autowired
    InvoiceRepository repository;

    @Autowired
    InvoiceQuotationResolve invoiceQuotationResolve;

    public Either<Throwable, Invoice> addCharge(EventNotification event) {
        val invoiceSearched = repository.getInvoiceByUserIdAndPeriod(event.getUserId(), Invoice.getPeriodDateOfInstant(event.getDate()));
        //if the invoice does not exist it is created
        val invoice = invoiceSearched.map(i -> i.getOrElse(Invoice.map().apply(event)));
        //Search the currency type
        val currencyType = List.of(CurrencyType.values()).find(c -> c.getIdentifier().equals(event.getCurrency())).toEither(new RuntimeException("The currencyType is invalid: " + event.getCurrency())).get();
        //Search the conversionFactor
        val conversionFactor = invoiceQuotationResolve.getQuotationByTypeAndDate(currencyType, event.getDate());
        //Add the charge in invoice
        val invoiceWithCharge = invoice.flatMap(i -> conversionFactor.flatMap(c -> i.addCharge(Charge.map().apply(event, c))));

        //Save the invoice
        return invoiceWithCharge.flatMap(i -> i.isNew() ? repository.save(i) : repository.update(i));
    }

    public Either<Throwable, Invoice> addPayment(PaymentInputApi paymentInputApi) {
        val invoiceSearched = repository.getInvoicesByUserIdAndChargesState(paymentInputApi.getUserId(), ChargeState.PENDING.getDescription());
        val invoiceFound = invoiceSearched.flatMap(i -> i.toEither(new DataNotFound("Invoice with debt for the user: " + paymentInputApi.getUserId() + " was not found")));
        val currencyType = List.of(CurrencyType.values()).find(t -> t.identifier.equals(paymentInputApi.getCurrency())).toEither(new Throwable("Type not supported: " + paymentInputApi.getCurrency()));
        val conversionFactor = currencyType.flatMap(c -> invoiceQuotationResolve.getQuotationByTypeAndDate(c, Instant.now()));
        val paymentHelper = conversionFactor.flatMap(c -> currencyType.map(t -> PaymentHelper.builder().userId(paymentInputApi.getUserId()).amount(paymentInputApi.getAmount()).currencyType(t).conversionFactor(c).build()));
        val invoiceWithPayment = invoiceFound.flatMap(i -> paymentHelper.flatMap(i::addPayment));
        return invoiceWithPayment.flatMap(i -> repository.update(i));
    }

    public Either<Throwable, Paginated<InvoiceApi>> findInvoices(InvoiceParametersApi parametersApi) {
        val parametersRepository = new InvoiceParametersRepository(parametersApi);
        return repository.findByPaginated(parametersRepository).map(r -> new Paginated<>(r.getItems().map(c -> InvoiceApi.map().apply(c)), r.getOffset(), r.getLimit(), r.getTotal()));
    }
}
