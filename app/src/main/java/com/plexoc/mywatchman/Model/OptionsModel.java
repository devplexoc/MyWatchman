package com.plexoc.mywatchman.Model;

public class OptionsModel {
    public int optionID;
    public String optionText;
    public boolean isNoteRequired;
    public int noteType;
    public boolean isNotOptional;

    public OptionsModel(int optionID, String optionText, boolean isNoteRequired, int noteType, boolean isNotOptional) {
        this.optionID = optionID;
        this.optionText = optionText;
        this.isNoteRequired = isNoteRequired;
        this.noteType = noteType;
        this.isNotOptional = isNotOptional;
    }

    public int getOptionID() {
        return optionID;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isNoteRequired() {
        return isNoteRequired;
    }

    public void setNoteRequired(boolean noteRequired) {
        isNoteRequired = noteRequired;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public boolean isNotOptional() {
        return isNotOptional;
    }

    public void setNotOptional(boolean notOptional) {
        isNotOptional = notOptional;
    }
}
