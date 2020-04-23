package com.plexoc.myapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.myapplication.Activity.HomeActivity;
import com.plexoc.myapplication.Model.Error;
import com.plexoc.myapplication.Model.Response;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;
import com.plexoc.myapplication.Utils.Prefs;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    private TextInputLayout textinput_username, textinput_password;
    private TextInputEditText edittext_username, edittext_password;
    private MaterialButton button_login;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radiobutton_mobilenumber, radiobutton_username;
    private String LoginUser;

    private AppCompatTextView textviewSignup, textview_countrycode, textview_username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textviewSignup = view.findViewById(R.id.textview_signup);
        textview_countrycode = view.findViewById(R.id.textview_countrycode);
        textview_username = view.findViewById(R.id.textview_username);
        button_login = view.findViewById(R.id.button_login);

        textinput_username = view.findViewById(R.id.textinput_username);
        textinput_password = view.findViewById(R.id.textinput_password);

        edittext_username = view.findViewById(R.id.edittext_username);
        edittext_password = view.findViewById(R.id.edittext_password);

        radioGroup = view.findViewById(R.id.radiogroup);
        radiobutton_mobilenumber = view.findViewById(R.id.radiobutton_mobilenumber);
        radiobutton_username = view.findViewById(R.id.radiobutton_username);

        textviewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SignupFragment(), null);
            }
        });


       /* radiobutton_mobilenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiobutton_mobilenumber.isChecked()){
                    radiobutton_mobilenumber.setChecked(true);
                    textview_countrycode.setVisibility(View.VISIBLE);
                    textview_username.setText("Mobilenumber");
                    radiobutton_username.setChecked(false);
                    edittext_username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                    LoginUser = textview_countrycode.getText().toString().trim()+edittext_username.getText().toString().trim();

                    button_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            closeKeybord();
                            if (doValidate()) {
                                LoginMobileNumber();
                            }
                        }
                    });
                }
                else {
                    //radiobutton_username.setChecked(false);
                    textview_countrycode.setVisibility(View.GONE);
                    textview_username.setText("Username");
                }
            }
        });*/

       /* radiobutton_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radiobutton_username.isChecked()){
                    radiobutton_username.setChecked(true);
                    textview_countrycode.setVisibility(View.GONE);
                    textview_username.setText("Username");
                    radiobutton_mobilenumber.setChecked(false);
                    LoginUser = edittext_username.getText().toString().trim();

                   *//* button_login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            closeKeybord();
                            if (doValidate()) {
                                LoginUsername();
                            }
                        }
                    });*//*
                }
                else {
                    //radiobutton_mobilenumber.setChecked(false);
                    textview_countrycode.setVisibility(View.VISIBLE);
                    textview_username.setText("Mobilenumber");
                }
            }
        });*/

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton_mobilenumber:
                        if (radiobutton_mobilenumber.isChecked()) {
                            radiobutton_mobilenumber.setChecked(true);
                            textview_countrycode.setVisibility(View.VISIBLE);
                            textview_username.setText("Mobilenumber");
                            radiobutton_username.setChecked(false);
                            edittext_username.setText("");
                            edittext_username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                            edittext_username.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            textview_countrycode.setVisibility(View.GONE);
                            textview_username.setText("Username");
                        }
                        break;
                    case R.id.radiobutton_username:
                        if (radiobutton_username.isChecked()) {
                            radiobutton_username.setChecked(true);
                            textview_countrycode.setVisibility(View.GONE);
                            textview_username.setText("Username");
                            radiobutton_mobilenumber.setChecked(false);
                            edittext_username.setText("");
                            edittext_username.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                            edittext_username.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else {
                            textview_countrycode.setVisibility(View.VISIBLE);
                            textview_username.setText("Mobilenumber");
                        }
                        break;
                }

            }
        });

        view.findViewById(R.id.textview_createnewaccount).setOnClickListener(v -> replaceFragment(new ForgotPasswordFragment(), null));

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeybord();
                if (doValidate()) {

                    int id = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(id);

                    if (radioButton.getText().toString().equals("Mobile Number")) {
                        LoginUser = textview_countrycode.getText().toString().trim() + edittext_username.getText().toString().trim();
                    } else
                        LoginUser = edittext_username.getText().toString().trim();

                    LoginMobileNumber();
                }
            }
        });

        return view;
    }

    private void LoginMobileNumber() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());

        getApiClient().Login(LoginUser, edittext_password.getText().toString().trim()).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        if(response.body().Item.UserType == 2){
                            Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                            user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                            Intent intent = new Intent(getContext(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            if (response.body().Item.PlanId == 0) {
                                replaceFragment(new PlansFragment(response.body().Item), null);
                            } else {
                                Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                                user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }

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

                edittext_username.setText("");
                edittext_password.setText("");

                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Login", t.getLocalizedMessage());
            }
        });
    }

    private boolean doValidate() {
        if (edittext_username.getText().toString().trim().isEmpty()) {
            textinput_username.setError("Please enter username");
            edittext_username.requestFocus();
            return false;
        } else {
            edittext_username.clearFocus();
            textinput_username.setErrorEnabled(false);
        }
        if (edittext_password.getText().toString().trim().isEmpty()) {
            textinput_password.setError("Please enter password");
            edittext_password.requestFocus();
            return false;
        } else {
            edittext_password.clearFocus();
            textinput_password.setErrorEnabled(false);
        }

        return true;
    }

}
