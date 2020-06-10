package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import com.plexoc.mywatchman.Utils.DrawerUtil;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {


    public ProfileFragment() {
        // Required empty public constructor
    }
    private Toolbar toolbar;
    private TextInputLayout textinput_profile_firstname,textinput_lastname,textinput_email,textinput_mobilenumber,
            textinput_password,textinput_username;
    private TextInputEditText edittext_profile_firstname,edittext_lastname,edittext_email,edittext_mobilenumber,
            edittext_password,edittext_username;
    private MaterialButton button_update;
    private AppCompatTextView textview_username,textview_email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textview_username = view.findViewById(R.id.textview_username);
        textview_email = view.findViewById(R.id.textview_email);

        textinput_profile_firstname = view.findViewById(R.id.textinput_profile_firstname);
        textinput_lastname = view.findViewById(R.id.textinput_lastname);
        textinput_email = view.findViewById(R.id.textinput_email);
        textinput_mobilenumber = view.findViewById(R.id.textinput_mobilenumber);
        textinput_password = view.findViewById(R.id.textinput_password);
        textinput_username = view.findViewById(R.id.textinput_username);

        edittext_profile_firstname = view.findViewById(R.id.edittext_profile_firstname);
        edittext_lastname = view.findViewById(R.id.edittext_lastname);
        edittext_email = view.findViewById(R.id.edittext_email);
        edittext_mobilenumber = view.findViewById(R.id.edittext_mobilenumber);
        edittext_password = view.findViewById(R.id.edittext_password);
        edittext_username = view.findViewById(R.id.edittext_username);

        textinput_mobilenumber.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));
        textinput_email.setBoxStrokeColor(getResources().getColor(R.color.colorPrimaryDark));

        button_update = view.findViewById(R.id.button_update);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(),toolbar,user);

        if(user.FirstName !=  null){
            edittext_profile_firstname.setText(user.FirstName);
        }if(user.LastName !=  null){
            edittext_lastname.setText(user.LastName);
        }if(user.Email !=  null){
            edittext_email.setText(user.Email);
        }if(user.Mobile !=  null){
            edittext_mobilenumber.setText(user.Mobile);
        }if(user.Password !=  null){
            edittext_password.setText(user.Password);
        }if(user.UserName != null){
            edittext_username.setText(user.UserName);
        }

        if(user.UserType == 2){
            textinput_username.setVisibility(View.GONE);
            textinput_email.setVisibility(View.GONE);
            edittext_username.setVisibility(View.GONE);
            edittext_email.setVisibility(View.GONE);
            textview_username.setVisibility(View.GONE);
            textview_email.setVisibility(View.GONE);
        }

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doValidate()){

                    user.FirstName = edittext_profile_firstname.getText().toString().trim();
                    user.LastName = edittext_lastname.getText().toString().trim();
                    user.UserName = edittext_username.getText().toString().trim();
                    user.Email = edittext_email.getText().toString().trim();
                    user.Mobile = edittext_mobilenumber.getText().toString().trim();

                    CallUpdateApi();
                }
            }
        });


        return view;
    }

    private void CallUpdateApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().SignUp(user).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if(response.code() == Constants.SuccessCode){
                    if(response.body().Item != null){

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }else {
                        showMessage(response.body().Message);
                    }
                }else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                }else {
                    try {
                        Error error = new Gson().fromJson(response.errorBody().string(), Error.class);
                        showMessage(error.Message);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Update Error",t.getLocalizedMessage());
            }
        });
    }

    private boolean doValidate() {
        if (edittext_profile_firstname.getText().toString().trim().isEmpty()) {
            textinput_profile_firstname.setError("Please enter first name");
            edittext_profile_firstname.requestFocus();
            return false;
        } else {
            edittext_profile_firstname.clearFocus();
            textinput_profile_firstname.setErrorEnabled(false);
        }

        if (edittext_lastname.getText().toString().trim().isEmpty()) {
            textinput_lastname.setError("Please enter last name");
            edittext_lastname.requestFocus();
            return false;
        } else {
            edittext_lastname.clearFocus();
            textinput_lastname.setErrorEnabled(false);
        }

        if (edittext_username.getText().toString().trim().isEmpty()) {
            textinput_username.setError("Please enter Username");
            edittext_username.requestFocus();
            return false;
        } else {
            edittext_username.clearFocus();
            textinput_username.setErrorEnabled(false);
        }

        if (edittext_email.getText().toString().trim().isEmpty()) {
            textinput_email.setError("Please enter email");
            edittext_email.requestFocus();
            return false;
        } else {
            edittext_email.clearFocus();
            textinput_email.setErrorEnabled(false);
        }

        if (!edittext_email.getText().toString().trim().isEmpty()) {
            if (!isValidEmail(edittext_email.getText().toString().trim())) {
                textinput_email.setError("Please enter valid email address");
                edittext_email.requestFocus();
                return false;
            } else {
                edittext_email.clearFocus();
                textinput_email.setErrorEnabled(false);
            }
        } else {
            edittext_email.clearFocus();
            textinput_email.setErrorEnabled(false);
        }

        if (edittext_mobilenumber.getText().toString().trim().isEmpty()) {
            textinput_mobilenumber.setError("Please enter mobile number");
            edittext_mobilenumber.requestFocus();
            return false;
        } else {
            edittext_mobilenumber.clearFocus();
            textinput_mobilenumber.setErrorEnabled(false);
        }

        return true;
    }

}
