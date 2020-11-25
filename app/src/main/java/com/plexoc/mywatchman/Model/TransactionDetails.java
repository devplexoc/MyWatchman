package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class TransactionDetails {
    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("PlanId")
    public int PlanId;

    @SerializedName("CardNumber")
    public String CardNumber;

    @SerializedName("CardCvv")
    public String CardCvv;

    @SerializedName("CardMonthYear")
    public String CardMonthYear;

    @SerializedName("Price")
    public String Price;

    @SerializedName("TransitionDate")
    public String TransitionDate;

    @SerializedName("ExpiryDate")
    public String ExpiryDate;

    @SerializedName("Mobile")
    public String Mobile;

    @SerializedName("PlanCategory")
    public String PlanCategory;

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("PlanDurationMonth")
    public int PlanDurationMonth;

    @SerializedName("CreatedDate")
    public String CreatedDate;


}
