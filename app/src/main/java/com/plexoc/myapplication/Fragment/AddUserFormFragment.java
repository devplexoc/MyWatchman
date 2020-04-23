package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.myapplication.Model.Error;
import com.plexoc.myapplication.Model.Response;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFormFragment extends BaseFragment {

    int id = 0;
    private String fName,lName,mobileNo,password;
    Toolbar toolbar;
    private MaterialButton button_submit;
    String str = "+213";
    int nbr = 4;

    private TextInputLayout textinput_user_name, textinput_user_lastname, textinput_user_mobilenumber, textinput_user_password;
    private TextInputEditText edittext_user_name, edittext_user_lastname, edittext_user_mobilenumber, edittext_user_password;
    private AppCompatTextView textview_user_password,textview_countrycode;

    private MaterialButton button_signup;

    public AddUserFormFragment(int id,String fName,String lName,String mobileNo,String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.mobileNo = mobileNo;
        this.password = password;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_form, container, false);

        textview_user_password = view.findViewById(R.id.textview_user_password);
        textview_countrycode = view.findViewById(R.id.textview_countrycode);

        textinput_user_name = view.findViewById(R.id.textinput_user_name);
        textinput_user_lastname = view.findViewById(R.id.textinput_user_lastname);
        textinput_user_mobilenumber = view.findViewById(R.id.textinput_user_mobilenumber);
        textinput_user_password = view.findViewById(R.id.textinput_user_password);

        edittext_user_name = view.findViewById(R.id.edittext_user_name);
        edittext_user_lastname = view.findViewById(R.id.edittext_user_lastname);
        edittext_user_mobilenumber = view.findViewById(R.id.edittext_user_mobilenumber);
        edittext_user_password = view.findViewById(R.id.edittext_user_password);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Users");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        if(fName != null){
            edittext_user_name.setText(fName);
        } if(lName != null){
            edittext_user_lastname.setText(lName);
        } if(mobileNo != null){
           /* String mobile = "";*/
            mobileNo = mobileNo.substring(4);
            edittext_user_mobilenumber.setText(mobileNo);
        } if(password != null){
            edittext_user_password.setText(password);
            textinput_user_password.setVisibility(View.GONE);
            textview_user_password.setVisibility(View.GONE);
            edittext_user_password.setVisibility(View.GONE);
        }

        button_submit = view.findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doValidate()){

                    user.ParentCustomer = user.Id;
                    user.FirstName = edittext_user_name.getText().toString().trim();
                    user.LastName = edittext_user_lastname.getText().toString().trim();
                    user.Mobile = textview_countrycode.getText().toString().trim()+edittext_user_mobilenumber.getText().toString().trim();
                    user.Password = edittext_user_password.getText().toString().trim();

                    CallSubUserSignupApi();
                }
            }
        });

        return view;
    }

    private void CallSubUserSignupApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().SubUserSignup(id,user.ParentCustomer,edittext_user_name.getText().toString().trim(),edittext_user_lastname.getText().toString().trim(),
                textview_countrycode.getText().toString().trim()+edittext_user_mobilenumber.getText().toString().trim(),edittext_user_password.getText().toString().trim()).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        closeKeybord();
                        replaceFragment(new AddUserFragment(),null);

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

    private boolean doValidate() {

        if (edittext_user_name.getText().toString().trim().isEmpty()) {
            textinput_user_name.setError("Please enter first name");
            edittext_user_name.requestFocus();
            return false;
        } else {
            edittext_user_name.clearFocus();
            textinput_user_name.setErrorEnabled(false);
        }

        if (edittext_user_lastname.getText().toString().trim().isEmpty()) {
            textinput_user_lastname.setError("Please enter last name");
            edittext_user_lastname.requestFocus();
            return false;
        } else {
            edittext_user_lastname.clearFocus();
            textinput_user_lastname.setErrorEnabled(false);
        }

        if (edittext_user_mobilenumber.getText().toString().trim().isEmpty()) {
            textinput_user_mobilenumber.setError("Please enter mobile number");
            edittext_user_mobilenumber.requestFocus();
            return false;
        } else {
            edittext_user_mobilenumber.clearFocus();
            textinput_user_mobilenumber.setErrorEnabled(false);
        }

        if (edittext_user_password.getText().toString().trim().isEmpty()) {
            textinput_user_password.setError("Please enter password");
            edittext_user_password.requestFocus();
            return false;
        } else {
            edittext_user_password.clearFocus();
            textinput_user_password.setErrorEnabled(false);
        }

        return true;
    }
}
