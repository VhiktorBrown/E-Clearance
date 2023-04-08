package com.theelitedevelopers.e_clearance.data.models;

import com.google.gson.annotations.SerializedName;

public class PayStackData {
    @SerializedName("authorization_url")
    String authorizationUrl;
    @SerializedName("access_code")
    String accessCode;
    @SerializedName("reference")
    String reference;

    public PayStackData(String authorizationUrl, String accessCode, String reference) {
        this.authorizationUrl = authorizationUrl;
        this.accessCode = accessCode;
        this.reference = reference;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
