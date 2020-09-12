package com.plexoc.mywatchman.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.plexoc.mywatchman.Interface.OTPReceiveListener;


public class MySMSBroadcastReceiver extends BroadcastReceiver {

  public OTPReceiveListener otpReceiveListener;
  public void initOtpReceiveListener(OTPReceiveListener otpReceiveListener) {
    this.otpReceiveListener = otpReceiveListener;
  }


  @Override
  public void onReceive(Context context, Intent intent) {
    if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
      Bundle extras = intent.getExtras();
      Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

      switch(status.getStatusCode()) {
        case CommonStatusCodes.SUCCESS:
          // Get SMS message contents
          String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
          Log.e("SMS",message);
          String[] strings=message.split("\n");
          message=strings[0].replace("is Your one time password for MyWatchman App.SHGkSWqJd5a",
                  "");
          if(otpReceiveListener!=null){
            otpReceiveListener.onOTPReceived(message);
          }
          // Extract one-time code from the message and complete verification
          // by sending the code back to your server.
          break;
        case CommonStatusCodes.TIMEOUT:
          // Waiting for SMS timed out (5 minutes)
          // Handle the error ...
          break;
      }
    }
  }
}