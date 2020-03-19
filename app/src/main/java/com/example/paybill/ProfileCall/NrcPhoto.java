package com.example.paybill.ProfileCall;

import com.google.gson.annotations.SerializedName;

public class NrcPhoto{
    @SerializedName("nrc_front_photo")
    public String nrc_front_photo;
    @SerializedName("nrc_back_photo")
    public String nrc_back_photo;
    @SerializedName("profile_name")
    public String profileName;
    @SerializedName("star")
    public Integer star;
    @SerializedName("member_role")
    public int memberRole;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(int memberRole) {
        this.memberRole = memberRole;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getNrc_front_photo() {
        return nrc_front_photo;
    }

    public void setNrc_front_photo(String nrc_front_photo) {
        this.nrc_front_photo = nrc_front_photo;
    }

    public String getNrc_back_photo() {
        return nrc_back_photo;
    }

    public void setNrc_back_photo(String nrc_back_photo) {
        this.nrc_back_photo = nrc_back_photo;
    }
}
