package com.plexoc.mywatchman.Model;

import com.google.gson.annotations.SerializedName;

public class QuestionAnswer {


    @SerializedName("Question")
    public String Question;


    @SerializedName("Answer")
    public String Answer;

    public QuestionAnswer(String question, String answer) {
        Question = question;
        Answer = answer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
