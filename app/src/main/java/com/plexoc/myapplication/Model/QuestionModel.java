package com.plexoc.myapplication.Model;

import java.util.List;

public class QuestionModel {

    public int questionId;
    public String question;
    public boolean isOptional;
    public List<OptionsModel> optionsModelList;
    public boolean hasOptions;
    public boolean hasNextQuestion;
    public boolean isNoteRequired;

    public QuestionModel(int questionId, String question, boolean isOptional, List<OptionsModel> optionsModelList,
                         boolean hasOptions, boolean hasNextQuestion,boolean isNoteRequired) {
        this.questionId = questionId;
        this.question = question;
        this.isOptional = isOptional;
        this.optionsModelList = optionsModelList;
        this.hasOptions = hasOptions;
        this.hasNextQuestion = hasNextQuestion;
        this.isNoteRequired = isNoteRequired;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public List<OptionsModel> getOptionsModelList() {
        return optionsModelList;
    }

    public void setOptionsModelList(List<OptionsModel> optionsModelList) {
        this.optionsModelList = optionsModelList;
    }

    public boolean isHasOptions() {
        return hasOptions;
    }

    public void setHasOptions(boolean hasOptions) {
        this.hasOptions = hasOptions;
    }

    public boolean isHasNextQuestion() {
        return hasNextQuestion;
    }

    public void setHasNextQuestion(boolean hasNextQuestion) {
        this.hasNextQuestion = hasNextQuestion;
    }

    public boolean isNoteRequired() {
        return isNoteRequired;
    }

    public void setNoteRequired(boolean noteRequired) {
        isNoteRequired = noteRequired;
    }
}
