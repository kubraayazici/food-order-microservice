package com.vanhuy.api_gateway.dto;

public class ValidTokenResponse {
    boolean valid;

    public ValidTokenResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
