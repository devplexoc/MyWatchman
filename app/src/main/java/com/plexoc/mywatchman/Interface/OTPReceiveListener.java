package com.plexoc.mywatchman.Interface;

public interface OTPReceiveListener {
    void onOTPReceived(String otp);
    void onOTPTimeOut();
}
