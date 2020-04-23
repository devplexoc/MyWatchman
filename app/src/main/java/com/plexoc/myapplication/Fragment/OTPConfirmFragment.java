package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.plexoc.myapplication.Model.Error;
import com.plexoc.myapplication.Model.Response;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */

public class OTPConfirmFragment extends BaseFragment {

    private User user;
    private OtpView otp_view;
    private AppCompatTextView textViewResendOTP;
    private AppCompatTextView textViewTimer;
    /*private TextInputEditText otp_view;
    private TextInputLayout textinput_otp_view;*/

    public OTPConfirmFragment(User user) {
        this.user = user;
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

                        CallSignupApi();
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
                        Toast.makeText(getContext(), "Resend Otp Successfully", Toast.LENGTH_SHORT).show();
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

}
