package com.example.paybill.Charge;

import com.google.gson.annotations.SerializedName;

public class Charge {
    @SerializedName("accountId")
    public String accountId;
    @SerializedName("description")
    public String description;
    @SerializedName("amount")
    public int amount;
    @SerializedName("currency")
    public String currency;
    @SerializedName("invoiceId")
    public String invoiceId;

    public Charge(String description, int amount) {
        this.description = description;
        this.amount = amount;
        this.currency="MMK";
    }

    public String getAccountId() {
        return accountId;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getInvoiceId(){ return invoiceId; }
}
