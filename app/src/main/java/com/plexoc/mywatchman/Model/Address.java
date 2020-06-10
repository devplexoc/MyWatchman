package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class Address {

  /*  public String name, address;

    public Address(String name, String address) {
        this.name = name;
        this.address = address;
    }*/

    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("AddressName")
    public String AddressName;

    @SerializedName("Address")
    public String Address;

    @SerializedName("Latitude")
    public String Latitude;

    @SerializedName("Longitude")
    public String Longitude;

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
