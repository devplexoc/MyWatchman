package com.plexoc.mywatchman.Model;

public class UserAddModel {
    public String name,mobileNumber,password;
    public boolean isShow;

    public UserAddModel(String name, String mobileNumber, String password, boolean isShow) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.isShow = isShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
