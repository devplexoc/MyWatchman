package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class Plan {
    /*String planPrice,planName,planType;

    public Plan(String planPrice, String planName,String planType) {
        this.planType = planType;
        this.planPrice = planPrice;
        this.planName = planName;

    }

    public String getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(String planPrice) {
        this.planPrice = planPrice;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }*/

    @SerializedName("Id")
    public int Id;

    @SerializedName("PlanCategory")
    public String PlanCategory;

    @SerializedName("Price")
    public int Price;

    @SerializedName("PlanDurationMonth")
    public String PlanDuration;

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
