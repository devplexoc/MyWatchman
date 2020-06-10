package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class TransactionHistory {

    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("PlanId")
    public int PlanId;

    @SerializedName("IsCancel")
    public boolean IsCancel;

    @SerializedName("TransactionAmount")
    public int TransactionAmount;

    @SerializedName("TransactionStartDate")
    public String TransactionStartDate;

    @SerializedName("TransactionEndDate")
    public String TransactionEndDate;

    @SerializedName("PlanName")
    public String PlanName;

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
}
