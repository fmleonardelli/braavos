package com.mercadolibre.braavos.invoices;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibre.braavos.invoices.charges.Charge;
import com.mercadolibre.braavos.invoices.charges.ChargeState;
import com.mercadolibre.braavos.invoices.kafka.EventNotification;
import com.mercadolibre.braavos.invoices.payments.model.PaymentHelper;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Either;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Invoice {
    String userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate periodDate;
    List<Charge> charges;
    Long version;

    public static Function1<EventNotification, Invoice> map() {
        return x -> new Invoice(x.getUserId(), x.getDate().atZone(ZoneId.systemDefault()).toLocalDate().withDayOfMonth(1), List.of(), 1L);
    }

    public Either<Throwable, Invoice> addCharge(Charge charge) {
        if (!charges.exists(c -> c.getEventId().equals(charge.getEventId()) && c.getEventType().equals(charge.getEventType()))) {
            return Either.right(toBuilder().charges(charges.append(charge)).build());
        }
        return Either.left(new RuntimeException("The charge already exists: " + charge.getEventId()));
    }

    public Either<Throwable, Invoice> addPayment(PaymentHelper paymentHelper) {
        val chargesPending = charges.filter(c -> c.status().equals(ChargeState.PENDING.getDescription())).sortBy(Charge::getDate);
        val chargesExcluded = charges.filter(c -> !c.status().equals(ChargeState.PENDING.getDescription()));
        val chargesNew = distributePayment(paymentHelper.getEffectiveAmount(), chargesPending, List.empty(), paymentHelper);
        return chargesNew.map(c -> toBuilder().charges(chargesExcluded.appendAll(c)).build());
    }

    /**
     * This method, with buildChargeWithPayment are recursive methods for distribute the payment in the existing charges
     * @param amount: value in the currency default
     * @param charges: the list of debt charges
     * @param resultingCharges: the list of paid charges
     * @param paymentHelper
     * @return
     */
    Either<Throwable, List<Charge>> distributePayment(Double amount, List<Charge> charges, List<Charge> resultingCharges, PaymentHelper paymentHelper) {
        //The amount was distributed in the charge/s
        if (Double.compare(amount, 0d) == 0) return Either.right(resultingCharges.appendAll(charges));
        else {
            //the payment amount was distributed in all charges
            if (charges.isEmpty()) return Either.left(new RuntimeException("The payment amount exceeds the debt"));
            else return buildChargeWithPayment(amount, charges, resultingCharges, paymentHelper);
        }
    }

    Either<Throwable, List<Charge>> buildChargeWithPayment(Double amount, List<Charge> charges, List<Charge> resultingCharges, PaymentHelper paymentHelper) {
        val differenceToCompleteCharge = charges.head().differenceToComplete();
        //The payment amount is greater than the amount of the remaining charge
        if (Double.compare(amount, differenceToCompleteCharge) == 1) {
            val chargeWithPayment = charges.head().addPayment(differenceToCompleteCharge, paymentHelper);
            return distributePayment(amount - differenceToCompleteCharge, charges.tail(), resultingCharges.append(chargeWithPayment), paymentHelper);
        } else {
            //The amount of the charge is greater than that of the payment
            val chargeWithPayment = charges.head().addPayment(amount, paymentHelper);
            //The payment amount was consumed by the charge
            return distributePayment(0d, charges.tail(), resultingCharges.append(chargeWithPayment), paymentHelper);
        }
    }

    @JsonIgnore
    public Boolean isNew() {
        if (List.of(0, 1).contains(charges.length())) {
            return true;
        }
        return false;
    }

    public static Instant getPeriodDateOfInstant(Instant date) {
        return date.atZone(ZoneId.of("UTC")).toLocalDate().
                        atStartOfDay().
                        withDayOfMonth(1).toInstant(ZoneOffset.UTC);
    }
}
