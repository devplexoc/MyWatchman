package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;


public class ForgotPasswordFragment extends BaseFragment {


    public ForgotPasswordFragment() {
    }


    private TextInputLayout inputlayout_forgotpassword_email;
    private TextInputEditText edittext_forgotpassword_email;
    private AppCompatTextView textview_forgot_password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        inputlayout_forgotpassword_email = view.findViewById(R.id.inputlayout_forgotpassword_email);
        edittext_forgotpassword_email = view.findViewById(R.id.edittext_forgotpassword_email);
        MaterialButton btn_verify_email = view.findViewById(R.id.btn_verify_email);
        textview_forgot_password = view.findViewById(R.id.textview_forgot_password);
        textview_forgot_password.setVisibility(View.GONE);

        btn_verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {
                    if (!isNetworkConnected()) {
                        Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LoadingDialog.showLoadingDialog(getContext());
                    getApiClient().Forgotpassword(edittext_forgotpassword_email.getText().toString().trim()).enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if (response.code() == Constants.SuccessCode) {
                                if (response.body().Item != null) {
                                    textview_forgot_password.setVisibility(View.VISIBLE);
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
                            edittext_forgotpassword_email.setText("");
                            LoadingDialog.cancelLoading();
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            LoadingDialog.cancelLoading();
                            Log.d("Forgotpassword", t.getLocalizedMessage());
                        }
                    });
                }
            }
        });

        return view;
    }

    private boolean doValidate() {
        if (edittext_forgotpassword_email.getText().toString().trim().isEmpty()) {
            inputlayout_forgotpassword_email.setError("Please enter email");
            edittext_forgotpassword_email.requestFocus();
            return false;
        } else {
            edittext_forgotpassword_email.clearFocus();
            inputlayout_forgotpassword_email.setErrorEnabled(false);
        }
        return true;
    }

}
