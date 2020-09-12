package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class SosType {
    @SerializedName("Id")
    public int Id;

    @SerializedName("Type")
    public String Type;

    @SerializedName("CreatedBy")
    public int CreatedBy;

    @SerializedName("SOSCount")
    public int SOSCount;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("ModifiedBy")
    public int ModifiedBy;

    @SerializedName("ModifiedDate")
    public String ModifiedDate;

}
