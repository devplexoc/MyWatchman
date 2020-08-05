package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class RaisedSOSUser {

    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int UserId;

    @SerializedName("Address")
    public String Address;

    @SerializedName("RoamingStaffId")
    public int ResponserId;

    @SerializedName("AddressName")
    public String AddressName;

    @SerializedName("AddressId")
    public int AddressId;

    @SerializedName("Note")
    public String Note;

    @SerializedName("StartDate")
    public String StartDate;

    @SerializedName("EndDate")
    public String EndDate;

    @SerializedName("StartTime")
    public String StartTime;

    @SerializedName("Status")
    public int Status;

    @SerializedName("IsActive")
    public boolean IsActive;

    @SerializedName("RoamingStaffName")
    public String ResponserName;

    @SerializedName("EndTime")
    public String EndTime;

    @SerializedName("CompletedSOSInMin")
    public String CompletedSOSInMin;

    @SerializedName("ValidOTP")
    public int ValidOTP;

    @SerializedName("Latitude")
    public String Latitude;

    @SerializedName("Longitude")
    public String Longitude;

    @SerializedName("Rating")
    public float Rating;

    @SerializedName("Comment")
    public String Comment;

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

    @SerializedName("SOSTypeId")
    public int SOSTypeId;

    @SerializedName("RoamingStaffLat")
    public String ResponderLat;

    @SerializedName("RoamingStaffLong")
    public String ResponderLong;

    @SerializedName("SOSType")
    public String SOSType;

    @SerializedName("RoamingStaffRating")
    public String ResponderRating;

    @SerializedName("CustomerRating")
    public String CustomerRating;

    @SerializedName("RoamingStaffImageUrl")
    public String RoamingStaffImageUrl;

    @SerializedName("CustomerProfilePicture")
    public String CustomerProfilePicture;

    @SerializedName("RoamingStaffPosition")
    public String RoamingStaffPosition;



}
