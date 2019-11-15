package com.mercadolibre.braavos.invoces.model;

import com.mercadolibre.braavos.invoices.CurrencyType;
import com.mercadolibre.braavos.invoices.Invoice;
import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.payments.PaymentInputApi;
import com.mercadolibre.braavos.invoices.payments.model.Payment;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static io.vavr.control.Option.none;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

public class InvoiceTest {

    @Test
    public void checkInvoiceProperties() {
        val invoice = Invoice.builder()
                .userId("facundo.leonardelli")
                .periodDate(LocalDate.now())
                .charges(List.empty())
                .version(1L).build();

        assertThat(invoice, hasProperty("userId"));
        assertThat(invoice, hasProperty("periodDate"));
        assertThat(invoice, hasProperty("charges"));
        assertThat(invoice, hasProperty("version"));
    }

    @Test
    public void addChargeExistentInInvoice() {
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
        Charge charge2 = Charge
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
        val invoice = Invoice.builder()
                .userId("")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1))
                .version(1L).build();
        assert invoice.addCharge(charge2).isLeft();
    }

    @Test
    public void addChargeNotExistentInInvoice() {
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
        Charge charge2 = Charge
                .builder()
                .eventId("1234")
                .eventType("VENTA")
                .amount(250d)
                .currency("ARS")
                .processedDate(Instant.now())
                .date(Instant.now())
                .conversionFactor(none())
                .payments(List.empty())
                .build();
        val invoice = Invoice.builder()
                .userId("")
                .periodDate(LocalDate.now())
                .charges(List.of(charge1))
                .version(1L).build();
        assert invoice.addCharge(charge2).isRight();
    }

    @Test
    public void debtGreaterThanPaymentAmount() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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

        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 == 0d;
    }

    @Test
    public void paymentAmountExceedsAmountCharges() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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

        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 != 0d;
    }

    @Test
    public void paymentAmountEqualsAmountCharges() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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

        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 == 0d;
    }

    @Test
    public void amountChargesExceedsAmountPayment() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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

        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 == 0d;
    }

    @Test
    public void amountTotalPaymentEqualsAmountCharges() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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


        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 == 0d;
    }

    @Test
    public void amountChargesPaymentExceedsAmountTotal() {
        PaymentInputApi paymentInputApi = PaymentInputApi
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
        val res = invoice.addPayment(new PaymentHelper(paymentInputApi.getUserId(), CurrencyType.ARS, paymentInputApi.getAmount(), Option.none()));
        assert res._1 == 0d;
    }
}
