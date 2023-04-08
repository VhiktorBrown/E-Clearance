package com.theelitedevelopers.e_clearance.data.models;

public class PaymentDetails {
    Double amount;
    String paymentFor;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentFor() {
        return paymentFor;
    }

    public void setPaymentFor(String paymentFor) {
        this.paymentFor = paymentFor;
    }
}
