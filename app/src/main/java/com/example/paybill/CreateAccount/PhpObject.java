package com.example.paybill.CreateAccount;

import com.google.gson.annotations.SerializedName;

public class PhpObject {
    @SerializedName("status")
    public Integer status;
    @SerializedName("member_id")
    public Integer memberId;
    @SerializedName("application_ssid")
    public String applicationssid;
    @SerializedName("username")
    public String username;
    @SerializedName("phone_no")
    public String phoneNo;
    @SerializedName("email")
    public String email;
    @SerializedName("address")
    public String address;
    @SerializedName("township")
    public String township;
    @SerializedName("state")
    public String state;
    @SerializedName("password")
    public String password;

    public PhpObject(String applicationssid, String username, String phoneNo, String email, String address, String township, String state, String password) {
        this.applicationssid = applicationssid;
        this.username = username;
        this.phoneNo = phoneNo;
        this.email = email;
        this.address = address;
        this.township = township;
        this.state = state;
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public String getApplicationssid() {
        return applicationssid;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getTownship() {
        return township;
    }

    public String getState() {
        return state;
    }

    public String getPassword() {
        return password;
    }
}
