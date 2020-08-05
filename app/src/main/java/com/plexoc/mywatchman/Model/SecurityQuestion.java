package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class SecurityQuestion {

    @SerializedName("Id")
    public int Id;

    @SerializedName("CustomerId")
    public int CustomerId;

    @SerializedName("SecurityQuestionId")
    public int SecurityQuestionId;

    @SerializedName("Questions")
    public String Questions;

    @SerializedName("Answer")
    public String Answer;


}
