package com.example.paybill.Utils;

public class Mail {
    public String senderId;
    public String invoiceId;
    public String amount;
    public String description;

    public Mail(String invoiceId,String senderId,String amount, String description) {
        this.amount = amount;
        this.senderId =senderId;
        this.invoiceId = invoiceId;
        this.description = description;
    }

    public String getSenderId() { return senderId; }

    public String getInvoiceId(){ return invoiceId;}

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
