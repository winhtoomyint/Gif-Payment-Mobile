package com.example.paybill.Utils;

public class Mail {
    public String invoiceId;
    public String amount;
    public String description;

    public Mail(String invoiceId,String amount, String description) {
        this.amount = amount;
        this.invoiceId = invoiceId;
        this.description = description;
    }
    public String getInvoiceId(){ return invoiceId;}

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
