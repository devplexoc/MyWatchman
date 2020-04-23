package com.plexoc.myapplication.Model;

import com.google.gson.annotations.SerializedName;

public class ChangePassword {
    @SerializedName("Id")
    public int Id;

    @SerializedName("PlanId")
    public int PlanId;

    @SerializedName("ParentId")
    public int ParentId;

    @SerializedName("SOSStatus")
    public int SOSStatus;

    @SerializedName("SosId")
    public int SosId;

    @SerializedName("SOSStatusNameName")
    public String SOSStatusNameName;

    @SerializedName("FlatId")
    public int FlatId;

    @SerializedName("FirstName")
    public String FirstName;

    @SerializedName("LastName")
    public String LastName;

    @SerializedName("ImageUrl")
    public String ImageUrl;

    @SerializedName("Email")
    public String Email;

    @SerializedName("Password")
    public String Password;

    @SerializedName("ConfirmPassword")
    public String ConfirmPassword;

    @SerializedName("Salt")
    public String Salt;

    @SerializedName("Phone")
    public String Phone;

    @SerializedName("UserType")
    public int UserType;

    @SerializedName("IsActive")
    public boolean IsActive;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("OwnerName1")
    public String OwnerName1;

    @SerializedName("OwnerName2")
    public String OwnerName2;

    @SerializedName("BussinessName")
    public String BussinessName;

    @SerializedName("BussinessType")
    public String BussinessType;

    @SerializedName("BussinessAddress")
    public String BussinessAddress;

    @SerializedName("NoOfEmplyees")
    public int NoOfEmplyees;

    @SerializedName("AvatarFolder")
    public String AvatarFolder;

    @SerializedName("DocumentPath")
    public String DocumentPath;

    @SerializedName("Path")
    public String Path;

    @SerializedName("CreateDate")
    public String CreateDate;

    @SerializedName("ModifiedDate")
    public String ModifiedDate;

    @SerializedName("CreatedBy")
    public String CreatedBy;

    @SerializedName("ModifiedBy")
    public String ModifiedBy;
}
