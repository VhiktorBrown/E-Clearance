package com.theelitedevelopers.e_clearance.data.models.dto;

import com.google.gson.annotations.SerializedName;

public class PayStackRequest {
    String email;
    String amount;

    public PayStackRequest(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
