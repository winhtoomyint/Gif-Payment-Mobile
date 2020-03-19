package com.example.paybill.ChargePhp;

import com.google.gson.annotations.SerializedName;

public class InvoiceForNumber {
    @SerializedName("invoiceNumber")
    public String invoiceNumber;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
