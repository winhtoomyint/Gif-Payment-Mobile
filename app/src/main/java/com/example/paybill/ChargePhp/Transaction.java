package com.example.paybill.ChargePhp;

import com.google.gson.annotations.SerializedName;

public class Transaction {
    @SerializedName("status")
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
