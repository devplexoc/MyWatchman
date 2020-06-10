package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.ChangePassword;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubUserResetPasswordFragment extends BaseFragment {

    private int Id;
    private String Password;
    Toolbar toolbar;

    public SubUserResetPasswordFragment(int Id, String Password) {
        this.Id = Id;
        this.Password = Password;
    }

    private TextInputLayout textinput_user_currentpassword, textinput_user_newpassword, textinput_user_confirmpassword;
    private TextInputEditText edittext_user_currentpassword, edittext_user_newpassword, edittext_user_confirmpassword;
    private MaterialButton button_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_user_reset_password, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Reset Password");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        textinput_user_currentpassword = view.findViewById(R.id.textinput_user_currentpassword);
        textinput_user_newpassword = view.findViewById(R.id.textinput_user_newpassword);
        textinput_user_confirmpassword = view.findViewById(R.id.textinput_user_confirmpassword);

        edittext_user_currentpassword = view.findViewById(R.id.edittext_user_currentpassword);
        edittext_user_newpassword = view.findViewById(R.id.edittext_user_newpassword);
        edittext_user_confirmpassword = view.findViewById(R.id.edittext_user_confirmpassword);

        button_submit = view.findViewById(R.id.button_submit);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {
                    CallUpdatePasswordApi();
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }

    private boolean doValidate() {

        boolean flg;

        closeKeybord();
        if (edittext_user_newpassword.getText().toString().isEmpty()) {
            textinput_user_newpassword.setError("Please Enter New Password");
            flg = false;
            return flg;
        } else {
            textinput_user_newpassword.setError("");
            flg = true;
        }


        if (!isValidPassword(edittext_user_newpassword.getText().toString().trim())) {
            textinput_user_newpassword.setError("password must be of minimum 8 characters and must include a capital letter , a special charcter and a digit");
            edittext_user_newpassword.requestFocus();
            flg = false;
            return flg;
        } else {
            textinput_user_newpassword.setErrorEnabled(false);
            edittext_user_newpassword.clearFocus();
            flg = true;
        }

        if (edittext_user_confirmpassword.getText().toString().isEmpty()) {
            textinput_user_confirmpassword.setError("Please Confirm Password");
            flg = false;
            return flg;
        } else {
            textinput_user_confirmpassword.setError("");
            flg = true;
        }

        if (!edittext_user_newpassword.getText().toString().equals(edittext_user_confirmpassword.getText().toString())) {
            showMessage("new password and Confirm new password does not match");
            textinput_user_confirmpassword.setError(" ");
            textinput_user_newpassword.setError(" ");
            flg = false;
            return flg;
        } else {
            flg = true;
            textinput_user_confirmpassword.setError("");
            textinput_user_newpassword.setError("");
        }


        return flg;
    }

    private void CallUpdatePasswordApi() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getChangePassword(Id, Password, 2).enqueue(new Callback<Response<ChangePassword>>() {
            @Override
            public void onResponse(Call<Response<ChangePassword>> call, retrofit2.Response<Response<ChangePassword>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        response.body().Item.Password = edittext_user_newpassword.getText().toString().trim();

                        /*Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);*/

                        //Toast.makeText(getContext(), "Password Change Successfully", Toast.LENGTH_SHORT).show();
                        //getActivity().onBackPressed();

                        /*Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();*/

                    } else
                        showMessage(response.body().Message);
                    //Toast.makeText(getContext(), response.body().Message, Toast.LENGTH_SHORT).show();

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

                edittext_user_newpassword.setText("");
                edittext_user_confirmpassword.setText("");
            }

            @Override
            public void onFailure(Call<Response<ChangePassword>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });

    }
}
