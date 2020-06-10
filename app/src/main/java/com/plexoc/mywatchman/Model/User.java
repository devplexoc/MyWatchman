package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("IsCancel")
    public boolean IsCancel;

    @SerializedName("ApprovedStatus")
    public boolean ApprovedStatus;

    @SerializedName("PlanId")
    public int PlanId;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Password")
    public String Password;

    @SerializedName("ConfirmPassword")
    public String ConfirmPassword;

    @SerializedName("Salt")
    public String Salt;

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("CreatedBy")
    public int CreatedBy;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("ModifiedBy")
    public int ModifiedBy;

    @SerializedName("ModifiedDate")
    public String ModifiedDate;

    @SerializedName("DeletedBy")
    public int DeletedBy;

    @SerializedName("DeletedDate")
    public String DeletedDate;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("PlanName")
    public String PlanName;

    @SerializedName("PlanPrice")
    public int PlanPrice;

    @SerializedName("PlanDuration")
    public int PlanDuration;

    @SerializedName("AddressCount")
    public int AddressCount;

    @SerializedName("PlanEndDate")
    public String PlanEndDate;

    @SerializedName("Otp")
    public String Otp;

    @SerializedName("UserType")
    public int UserType;

    @SerializedName("ParentCustomer")
    public int ParentCustomer;

    @SerializedName("isadmin")
    public boolean isadmin;

    @SerializedName("DeviceToken")
    public String DeviceToken;

    @SerializedName("DeviceInfo")
    public String DeviceInfo;
}
