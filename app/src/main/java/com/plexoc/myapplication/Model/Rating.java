package com.plexoc.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("Id")
    public int Id;

    @SerializedName("RoamingStaffId")
    public int RoamingStaffId;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("SOSId")
    public int SOSId;

    @SerializedName("Rating")
    public String Rating;

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
