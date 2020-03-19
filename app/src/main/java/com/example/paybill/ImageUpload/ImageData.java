package com.example.paybill.ImageUpload;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("status")
    public String status;
    @SerializedName("member_id")
    public String member_id;
    @SerializedName("application_ssid")
    public String application_ssid;

    public String getApplication_ssid() {
        return application_ssid;
    }

    public void setApplication_ssid(String application_ssid) {
        this.application_ssid = application_ssid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
