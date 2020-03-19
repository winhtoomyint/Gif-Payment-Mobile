package com.example.paybill.CreateAccount;

import com.google.gson.annotations.SerializedName;

public class Create {
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("address1")
    public String address1;
    @SerializedName("city")
    public String city;
    @SerializedName("state")
    public String state;
    @SerializedName("currency")
    public String currency;

    public Create(String name, String email, String address1, String city, String state) {
        this.name = name;
        this.email = email;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.currency = "MMK";
    }
}
