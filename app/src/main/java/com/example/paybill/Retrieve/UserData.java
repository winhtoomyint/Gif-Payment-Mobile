package com.example.paybill.Retrieve;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("accountId")
    public String accountId;
    @SerializedName("name")
    public String name;
    @SerializedName("externalKey")
    public String externalKey;
    @SerializedName("email")
    public String email;
    @SerializedName("currency")
    public String currency;


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExternalKey() {
        return externalKey;
    }

    /*public UserDetailData(String accountId, String name, String externalKey, String email, String currency) {
        this.accountId = accountId;
        this.name = name;
        this.externalKey = externalKey;
        this.email = email;
        this.currency = currency;
    }*/
}
