package com.example.paybill.CreateAccount;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("currency")
    public String currency;
    @SerializedName("externalKey")
    public String externalKey;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public User(String name, String external_key ,String email) {
        this.name = name;
        this.email = email;
        this.currency = "MMK";
        this.externalKey = external_key;
    }
}
