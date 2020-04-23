package com.plexoc.myapplication.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListResponse<T> {
    public ListResponse(int index) {
    }

    @SerializedName("TotalRecords")
    @Expose
    public Integer TotalRecords;

    @SerializedName("Values")
    @Expose
    public List<T> Values;

    @SerializedName("Offset")
    @Expose
    public Integer Offset;

    @SerializedName("Limit")
    @Expose
    public Integer Limit;

    @SerializedName("IsOffsetProvided")
    @Expose
    public boolean IsOffsetProvided;

    @SerializedName("Page")
    @Expose
    public Integer Page;

    @SerializedName("PageSize")
    @Expose
    public Integer PageSize;

    @SerializedName("SortBy")
    @Expose
    public String SortBy;

    @SerializedName("TotalCount")
    @Expose
    public Integer TotalCount;

    @SerializedName("SortDirection")
    @Expose
    public String SortDirection;

    @SerializedName("IsPageProvided")
    @Expose
    public boolean IsPageProvided;
}
