package com.mercadolibre.braavos.invoices;

import com.mercadolibre.braavos.invoices.api.*;
import com.mercadolibre.braavos.invoices.api.error.ValidationError;
import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.charges.ChargeState;
import com.mercadolibre.braavos.invoices.kafka.EventNotification;
import com.mercadolibre.braavos.invoices.payments.PaymentInputApi;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import com.mercadolibre.braavos.invoices.repo.InvoiceParametersForSummaryRepository;
import com.mercadolibre.braavos.invoices.repo.InvoiceParametersRepository;
import com.mercadolibre.braavos.invoices.repo.InvoiceRepository;
import io.reactivex.Single;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

import static io.vavr.API.*;

@Service
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceService implements InvoiceValidator {

    @Autowired
    InvoiceRepository repository;

    @Autowired
    InvoiceQuotationResolve invoiceQuotationResolve;

    public Either<Throwable, Invoice> addCharge(EventNotification event) {
        val invoiceSearched = repository.getInvoiceByUserIdAndPeriod(event.getUserId(), Invoice.getPeriodDateOfInstant(event.getDate()));
        //if the invoice does not exist it is created
        val invoice = invoiceSearched.map(i -> i.getOrElse(Invoice.map().apply(event)));
        //Search the currency type
        val currencyType = invoice.flatMap(i -> i.checkCurrency(event.getCurrency()));
        //Search the conversionFactor
        val conversionFactor = currencyType.flatMap(c -> invoiceQuotationResolve.getQuotationByTypeAndDate(c, event.getDate()));
        //Add the charge in invoice
        val invoiceWithCharge = invoice.flatMap(i -> conversionFactor.flatMap(c -> i.addCharge(Charge.map().apply(event, c))));
        //Save the invoice
        return invoiceWithCharge.flatMap(i -> i.isNew() ? repository.save(i) : repository.update(i));
    }

    public Either<Throwable, Paginated<InvoiceApi>> findInvoices(InvoiceParametersApi parametersApi) {
        val parametersRepository = new InvoiceParametersRepository(parametersApi);
        return repository.findByPaginated(parametersRepository).map(r -> new Paginated<>(r.getItems().map(c -> InvoiceApi.map().apply(c)), r.getOffset(), r.getLimit(), r.getTotal()));
    }

    public Either<Throwable, InvoicesSummaryApi> findInvoicesSummary(InvoiceParametersForSummaryApi parametersApi) {
        val parameters = new InvoiceParametersForSummaryRepository(parametersApi);
        val invoices = repository.findBy(parameters);
        //For each invoice the summary is calculated
        val summary = invoices.map(i -> i.map(Invoice::getSummary));
        //Sum of summaries
        val total = summary.map(s -> s.foldLeft(
                Tuple(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                (seed, elem) ->  Tuple(
                        seed._1.add(elem._1),
                        seed._2.add(elem._2),
                        seed._3.add(elem._3))));
        return total.map(t -> new InvoicesSummaryApi(parametersApi.getUserId(), t._1, t._2, t._3));
    }

    public Either<Throwable, Boolean> addPayment(PaymentInputApi payment) {
        return generatePayment(payment);
    }

    public Single<Boolean> addPaymentReactive(PaymentInputApi paymentInputApi) {
        return Single.create(singleSubscriber -> {
            val newInvoice = generatePayment(paymentInputApi);
            if (newInvoice.isRight()) {
                singleSubscriber.onSuccess(newInvoice.get());
            } else {
                singleSubscriber.onError(newInvoice.getLeft());
            }
        });
    }

    Either<Throwable, Boolean> generatePayment(PaymentInputApi paymentInputApi) {
        //Search the invoice that has a pending charge for the user
        val invoicesSearched = repository.getInvoicesByUserIdAndChargesState(paymentInputApi.getUserId(), ChargeState.PENDING.getDescription());
        //if not found, a left is returned
        val invoicesFound = invoicesSearched.flatMap(this::checkInvocesDebt);
        //Chech te payment amount
        val amountPayment = invoicesFound.flatMap(i -> this.checkAmount(paymentInputApi.getAmount()));
        //Check the payment currency
        val currencyType = amountPayment.flatMap(i -> checkCurrency(paymentInputApi.getCurrency()));
        //Find the conversion factor
        val conversionFactor = currencyType.flatMap(c -> invoiceQuotationResolve.getQuotationByTypeAndDate(c, Instant.now()));
        //Build a helper with necessary payment data
        val paymentHelper = conversionFactor.flatMap(c -> currencyType.map(t -> PaymentHelper.builder().userId(paymentInputApi.getUserId()).amount(paymentInputApi.getAmount()).currencyType(t).conversionFactor(c).build()));
        //Add the payment to invoice
        val res = invoicesSearched.flatMap(i -> paymentHelper.flatMap(p -> distributePaymentInInvoices(paymentInputApi.getAmount(), i, List.empty(), p)));
        //Update the invoice
        return res.flatMap(r -> repository.updateMany(r));
    }

    Either<Throwable, List<Invoice>> distributePaymentInInvoices(BigDecimal amount, List<Invoice> invoices, List<Invoice> invoicesResulting, PaymentHelper paymentHelper) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return Right(invoicesResulting);
        } else {
            if (invoices.isEmpty()) {
                return Left(new ValidationError("The payment amount exceeds the debt"));
            } else {
                val res = invoices.head().addPayment(paymentHelper);
                val newPaymentHelper = paymentHelper.withAmountAndCurrencyType(res._1, CurrencyType.ARS, Option.none());
                return distributePaymentInInvoices(res._1, invoices.tail(), invoicesResulting.append(res._2), newPaymentHelper);
            }
        }
    }
}
