package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class Notifiaction {

    @SerializedName("Id")
    public int Id;

    @SerializedName("UserId")
    public int UserId;

    @SerializedName("ResponderId")
    public int ResponderId;

    @SerializedName("Title")
    public String Title;

    @SerializedName("Description")
    public String Description;

    @SerializedName("CreatedDate")
    public String CreatedDate;

    @SerializedName("ReadDateTime")
    public String ReadDateTime;


}
