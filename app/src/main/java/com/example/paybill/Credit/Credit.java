package com.example.paybill.Credit;

import com.google.gson.annotations.SerializedName;

public class Credit {
    @SerializedName("creditAmount")
    public double creditAmount;
    @SerializedName("currency")
    public String currency;
    @SerializedName("accountId")
    public String accountId;
    @SerializedName("description")
    public String description;

    public Credit(double creditAmount,String accountId, String description) {
        this.creditAmount = creditAmount;
        this.currency = "MMK";
        this.accountId = accountId;
        this.description = description;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }
}
