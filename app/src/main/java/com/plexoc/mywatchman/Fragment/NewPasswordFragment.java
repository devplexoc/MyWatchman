package com.plexoc.mywatchman.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Activity.LoginSignupActivity;
import com.plexoc.mywatchman.Model.ChangePassword;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPasswordFragment extends BaseFragment {

    private TextInputLayout textinput_new_password, textinput_confirmpasword;
    private TextInputEditText edittext_new_password, edittext_confirmpasword;
    private AppCompatButton button_save;
    private User user;

    public NewPasswordFragment(User user) {
        this.user = user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_password, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitle("Change Password");
        toolbar.setNavigationOnClickListener(view1 -> getActivity().onBackPressed());

        textinput_new_password = view.findViewById(R.id.textinput_new_password);
        textinput_confirmpasword = view.findViewById(R.id.textinput_confirmpasword);
        edittext_new_password = view.findViewById(R.id.edittext_new_password);
        edittext_confirmpasword = view.findViewById(R.id.edittext_confirmpasword);
        button_save = view.findViewById(R.id.button_save);


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {
                    //user.Password = edittext_confirmpasword.getText().toString().trim();
                    CallUpdatePasswordApi();
                }
            }
        });

        return view;
    }

    private void CallUpdatePasswordApi() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getChangePassword(user.CustomerId, edittext_confirmpasword.getText().toString().trim(), 2).enqueue(new Callback<Response<ChangePassword>>() {
            @Override
            public void onResponse(Call<Response<ChangePassword>> call, retrofit2.Response<Response<ChangePassword>> response) {
                if (response.code() == Constants.SuccessCode) {
                        /*Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);*/

                    Toast.makeText(getContext(), "Password Change Successfully", Toast.LENGTH_SHORT).show();
                    //getActivity().finish();

                    Intent intent = new Intent(getContext(), LoginSignupActivity.class);
                    startActivity(intent);
                    getActivity().finish();
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

                edittext_new_password.setText("");
                edittext_confirmpasword.setText("");
            }

            @Override
            public void onFailure(Call<Response<ChangePassword>> call, Throwable t) {
                LoadingDialog.cancelLoading();
            }
        });

    }

    private boolean doValidate() {

        boolean flg;

        closeKeybord();

        if (edittext_new_password.getText().toString().isEmpty()) {
            textinput_new_password.setError("Please Enter New Password");
            flg = false;
            return flg;
        } else {
            textinput_new_password.setError("");
            flg = true;
        }

        if (edittext_confirmpasword.getText().toString().isEmpty()) {
            textinput_confirmpasword.setError("Please Confirm Password");
            flg = false;
            return flg;
        } else {
            textinput_confirmpasword.setError("");
            flg = true;
        }

        if (!edittext_new_password.getText().toString().equals(edittext_confirmpasword.getText().toString())) {
            showMessage("New password and Confirm new password does not match");
            textinput_confirmpasword.setError(" ");
            textinput_new_password.setError(" ");
            flg = false;
            return flg;
        } else {
            flg = true;
            textinput_confirmpasword.setError("");
            textinput_new_password.setError("");
        }
        return flg;
    }
}
