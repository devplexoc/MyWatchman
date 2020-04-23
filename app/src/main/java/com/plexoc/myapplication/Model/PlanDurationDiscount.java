package com.plexoc.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class PlanDurationDiscount {
    @SerializedName("Id")
    public int Id;

    @SerializedName("Name")
    public String Name;

    @SerializedName("DiscountPercentage")
    public int DiscountPercentage;

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
