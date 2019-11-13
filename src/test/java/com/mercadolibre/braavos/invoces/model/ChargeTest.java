package com.mercadolibre.braavos.invoces.model;

import com.mercadolibre.braavos.invoices.CurrencyType;
import com.mercadolibre.braavos.invoices.Invoice;
import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import com.mercadolibre.braavos.invoices.payments.PaymentApi;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;

import static io.vavr.control.Option.none;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class ChargeTest {

    private static Logger logger = LoggerFactory.getLogger(ChargeTest.class);

    @Test
    public void checkChargeProperties() {
        Charge charge = Charge
                .builder()
                .eventId("")
                .eventType("")
                .amount(0d)
                .currency("")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(Option.none())
                .payments(List.empty())
                .build();

        assertThat(charge, hasProperty("eventId"));
        assertThat(charge, hasProperty("eventType"));
        assertThat(charge, hasProperty("amount"));
        assertThat(charge, hasProperty("currency"));
        assertThat(charge, hasProperty("processedDate"));
        assertThat(charge, hasProperty("date"));
        assertThat(charge, hasProperty("conversionFactor"));
        assertThat(charge, hasProperty("processedDate"));
        assertThat(charge, hasProperty("payments"));
    }

    @Test
    public void debtGreaterThanPaymentAmount() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(48d).build();
        Charge charge1 = Charge
                .builder()
                .eventId("1234")
                .eventType("CLASIFICADO")
                .amount(250d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1))
                .version(1L)
                .build();

         val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
         assert res.isRight();
    }

    @Test
    public void paymentAmountExceedsAmountCharges() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(270d).build();
        Charge charge1 = Charge
                .builder()
                .eventId("1234")
                .eventType("CLASIFICADO")
                .amount(130d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        Charge charge2 = Charge
                .builder()
                .eventId("123")
                .eventType("CLASIFICADO")
                .amount(130d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1, charge2))
                .version(1L)
                .build();

        val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
        assert res.isLeft();
    }

    @Test
    public void paymentAmountEqualsAmountCharges() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(250d).build();
        Charge charge1 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(125d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        Charge charge2 = Charge
                .builder()
                .eventId("1234")
                .eventType("CLASIFICADO")
                .amount(125d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1, charge2))
                .version(1L)
                .build();

        val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
        assert res.isRight();
    }

    @Test
    public void amountChargesExceedsAmountPayment() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(250d).build();

        Payment payment = Payment
                .builder()
                .amount(25d)
                .currency("ARS")
                .date(Instant.now())
                .conversionFactor(none())
                .build();
        Charge charge1 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(300d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.of(payment))
                .build();

        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1))
                .version(1L)
                .build();

        val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
        assert res.isRight();
    }

    @Test
    public void amountTotalPaymentEqualsAmountCharges() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(250d).build();

        Payment payment = Payment
                .builder()
                .amount(250d)
                .currency("ARS")
                .date(Instant.now())
                .conversionFactor(none())
                .build();
        Charge charge1 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(300d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.of(payment))
                .build();
        Charge charge2 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(200d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.of())
                .build();

        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1, charge2))
                .version(1L)
                .build();

        val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
        assert res.isRight();
    }

    @Test
    public void amountChargesPaymentExceedsAmountTotal() {
        PaymentApi paymentApi = PaymentApi
                .builder()
                .userId("facundo.leonardelli")
                .currency("ARS")
                .amount(150d).build();

        Payment payment = Payment
                .builder()
                .amount(250d)
                .currency("ARS")
                .date(Instant.now())
                .conversionFactor(none())
                .build();
        Charge charge1 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(300d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.of(payment))
                .build();
        Charge charge2 = Charge
                .builder()
                .eventId("1")
                .eventType("CLASIFICADO")
                .amount(200d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.of())
                .build();

        Invoice invoice = Invoice
                .builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1, charge2))
                .version(1L)
                .build();

        val res = invoice.addPayment(new PaymentHelper(paymentApi.getUserId(), CurrencyType.ARS, paymentApi.getAmount(), Option.none()));
        assert res.isRight();
    }
}
