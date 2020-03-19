package com.example.paybill.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Invoice {
    @SerializedName("amount")
    private int amount;
    @SerializedName("currency")
    private String currency;
    @SerializedName("creditAdj")
    private int creditAdj;
    @SerializedName("invoiceDate")
    private String invoiceDate;
    @SerializedName("status")
    private String status;
    @SerializedName("items")
    public List<invoice_desc> invoice_detail=new ArrayList<>();


    public Invoice(int amount, String currency, int creditAdj, String invoiceDate,String status ) {
        this.amount = amount;
        this.currency = currency;
        this.creditAdj = creditAdj;
        this.invoiceDate = invoiceDate;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }



    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCreditAdj() {
        return creditAdj;
    }

    public void setCreditAdj(int creditAdj) {
        this.creditAdj = creditAdj;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public class invoice_desc {
        @SerializedName("description")
        private String description;

        public invoice_desc(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
