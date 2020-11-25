package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class PlanDetails {

    @SerializedName("Id")
    public int Id;

    @SerializedName("PlanCategory")
    public String PlanCategory;

    @SerializedName("ExpiryDate")
    public String ExpiryDate;

    @SerializedName("TransitionDate")
    public String TransitionDate;

    @SerializedName("Price")
    public String Price;

    @SerializedName("AddressCount")
    public int AddressCount;

    @SerializedName("SOSCount")
    public int SOSCount;

    @SerializedName("SOSType")
    public String SOSType;

    @SerializedName("PlanDurationMonth")
    public int PlanDurationMonth;

    @SerializedName("TrialPeriod")
    public int TrialPeriod;

    @SerializedName("SubAccountCount")
    public int SubAccountCount;

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

    @SerializedName("DiscountPercentage")
    public String DiscountPercentage;

    @SerializedName("Name")
    public String Name;

    @SerializedName("PlanDetails")
    public String PlanDetails;
}
