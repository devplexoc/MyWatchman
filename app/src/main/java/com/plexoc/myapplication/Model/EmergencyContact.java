package com.plexoc.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class EmergencyContact {
    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("Approvedstatus")
    public boolean Approvedstatus;

    @SerializedName("ContactName")
    public String ContactName;

    @SerializedName("ContactPhone")
    public String ContactPhone;

    @SerializedName("ContactEmail")
    public String ContactEmail;

    @SerializedName("Relation")
    public String Relation;

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
    public int DeletedDate;

}
