package com.theelitedevelopers.e_clearance.data.models.dto;

import com.google.gson.annotations.SerializedName;
import com.theelitedevelopers.e_clearance.data.models.PaymentDetails;

public class PayStackResponseDto {
    private Boolean success;
    private String message;
    private String flwRef;
    private PaymentDetails paymentDetails;

    public PayStackResponseDto(){}

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFlwRef() {
        return flwRef;
    }

    public void setFlwRef(String flwRef) {
        this.flwRef = flwRef;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
