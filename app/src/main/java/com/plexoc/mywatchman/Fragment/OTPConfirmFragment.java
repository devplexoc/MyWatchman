package com.plexoc.mywatchman.Fragment;


import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.plexoc.mywatchman.Interface.OTPReceiveListener;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.receiver.MySMSBroadcastReceiver;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */

public class OTPConfirmFragment extends BaseFragment implements OTPReceiveListener {

    private User user;
    private OtpView otp_view;
    private AppCompatTextView textViewResendOTP;
    private AppCompatTextView textViewTimer;
    private boolean isForgotPassword;
    private MySMSBroadcastReceiver mSmsBroadcastReceive;
    /*private TextInputEditText otp_view;
    private TextInputLayout textinput_otp_view;*/

    public OTPConfirmFragment(User user, boolean isForgotPassword) {
        this.user = user;
        this.isForgotPassword = isForgotPassword;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otpconfirm, container, false);

        AppCompatTextView textview_mobilenumber = view.findViewById(R.id.textview_mobilenumber);
        MaterialButton button_varify = view.findViewById(R.id.button_varify);
        AppCompatImageView imageview_back = view.findViewById(R.id.imageview_back);
        otp_view = view.findViewById(R.id.otp_view);
        otp_view.requestFocus();
        //textinput_otp_view = view.findViewById(R.id.textinput_otp_view);

        if (user.Mobile != null) {
            textview_mobilenumber.setText(user.Mobile);
        }

        textViewTimer = view.findViewById(R.id.textview_timer);
        textViewResendOTP = view.findViewById(R.id.textview_resendotp);
        textViewResendOTP.setOnClickListener(v -> {
            ResendOTP();
        });
        startTimer();
        button_varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {
                    if (otp_view.getText().toString().trim().equals(user.Otp)) {

                        if (!isForgotPassword)
                            //  CallSignupApi();
                            replaceFragment(new SecurityQuestionFragment(user), null);
                        else {
                            replaceFragment(new NewPasswordFragment(user), null);
                        }
                        //replaceFragment(new PlansFragment(user), null);
                    } else
                        Toast.makeText(getContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();

                }
            }
        });

        imageview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void ResendOTP() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().ResendOTP(user.Mobile, user.Otp).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        textViewResendOTP.setVisibility(View.INVISIBLE);
                        textViewTimer.setVisibility(View.VISIBLE);
                        startTimer();
                        //Toast.makeText(getContext(), "Resend Otp Successfully", Toast.LENGTH_SHORT).show();
                    } else
                        showMessage(response.body().Message);
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {

            }
        });
    }

    private void CallSignupApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().SignUp(user).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        closeKeybord();

                        replaceFragment(new PlansFragment(response.body().Item), null);

                        //replaceFragment(new OTPConfirmFragment(response.body().Item),null);

                        /*Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);*/
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Signup Fail", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private void startTimer() {

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "Resend OTP in %02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                textViewTimer.setText(text);
            }

            public void onFinish() {
                textViewResendOTP.setVisibility(View.VISIBLE);
                textViewTimer.setVisibility(View.GONE);
            }

        }.start();

    }

    private boolean doValidate() {
        if (otp_view.getText().toString().trim().isEmpty()) {
            otp_view.setError("Please enter OTP");
            otp_view.requestFocus();
            return false;
        } else {
            otp_view.clearFocus();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        regReceiver();
    }

    private void regReceiver() {
        startSmsListener();
        mSmsBroadcastReceive = new MySMSBroadcastReceiver();
        mSmsBroadcastReceive.initOtpReceiveListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().getApplicationContext().registerReceiver(mSmsBroadcastReceive, intentFilter);
    }

    private void startSmsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(getContext()  /* context */);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }

    @Override
    public void onOTPReceived(String otp) {
        String[] OTP = otp.split(" ");
        try {
            otp_view.setText(OTP[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

       // Toast.makeText(getContext(), "OTP:-" + OTP[1], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mSmsBroadcastReceive != null) {
                getActivity().unregisterReceiver(mSmsBroadcastReceive);
            }
        } catch (Exception e) {

        }
    }
}
