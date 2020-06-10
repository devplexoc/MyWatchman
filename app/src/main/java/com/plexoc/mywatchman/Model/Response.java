package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response<T> {

    @SerializedName("Code")
    @Expose
    public Integer Code;
    @SerializedName("Message")
    @Expose
    public String Message;
    @SerializedName("Item")
    @Expose
    public T Item;
    @SerializedName("IsValid")
    @Expose
    public Boolean IsValid;
    @SerializedName("IsSuccessStatusCode")
    @Expose
    public Boolean IsSuccessStatusCode;
}
