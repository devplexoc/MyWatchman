package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionAnswerBody {


    @SerializedName("QuestionAnswer")
    public List<QuestionAnswer> questionAnswerList;

    public QuestionAnswerBody(List<QuestionAnswer> questionAnswerList) {
        this.questionAnswerList = questionAnswerList;
    }
}
