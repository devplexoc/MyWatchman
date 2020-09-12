package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.CountyMaster;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ForgotPasswordFragment extends BaseFragment {


    public ForgotPasswordFragment() {
    }

    private ArrayList<String> arrayListSpinnerCountry = new ArrayList<>();
    private List<CountyMaster> countyMasterList;
    private String CountryItem;
    private AppCompatSpinner spinner_countrycode;

    private TextInputLayout inputlayout_forgotpassword_email;
    private TextInputEditText edittext_forgotpassword_email;
    private AppCompatTextView textview_forgot_password, textview_countrycode, textview_username;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private RadioButton radiobutton_mobilenumber, radiobutton_email;
    private String LoginUser;
    private AppCompatTextView textViewone, textViewtwo, textViewthree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        inputlayout_forgotpassword_email = view.findViewById(R.id.inputlayout_forgotpassword_email);
        edittext_forgotpassword_email = view.findViewById(R.id.edittext_forgotpassword_email);
        MaterialButton btn_verify_email = view.findViewById(R.id.btn_verify_email);
        textview_username = view.findViewById(R.id.textview_username);
        textview_countrycode = view.findViewById(R.id.textview_countrycode);
        textview_forgot_password = view.findViewById(R.id.textview_forgot_password);
        textViewone = view.findViewById(R.id.textview_one);
        textViewtwo = view.findViewById(R.id.textview_two);
        textViewthree = view.findViewById(R.id.textview_three);
        textview_forgot_password.setVisibility(View.GONE);

        spinner_countrycode = view.findViewById(R.id.spinner_countrycode);

        radioGroup = view.findViewById(R.id.radiogroup);
        radiobutton_mobilenumber = view.findViewById(R.id.radiobutton_mobilenumber);
        radiobutton_email = view.findViewById(R.id.radiobutton_email);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton_mobilenumber:
                        if (radiobutton_mobilenumber.isChecked()) {
                            radiobutton_mobilenumber.setChecked(true);
                            //textview_countrycode.setVisibility(View.VISIBLE);
                            spinner_countrycode.setVisibility(View.VISIBLE);
                            textview_username.setText("Mobilenumber");
                            radiobutton_email.setChecked(false);
                            inputlayout_forgotpassword_email.setError(" ");
                            edittext_forgotpassword_email.setText("");
                            textViewone.setVisibility(View.INVISIBLE);
                            textViewtwo.setVisibility(View.INVISIBLE);
                            textViewthree.setVisibility(View.INVISIBLE);
                            edittext_forgotpassword_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                            edittext_forgotpassword_email.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else {
                            spinner_countrycode.setVisibility(View.GONE);
                            //textview_countrycode.setVisibility(View.GONE);
                            textview_username.setText("Username");
                        }
                        break;
                    case R.id.radiobutton_email:
                        if (radiobutton_email.isChecked()) {
                            radiobutton_email.setChecked(true);
                            //textview_countrycode.setVisibility(View.GONE);
                            spinner_countrycode.setVisibility(View.GONE);
                            textview_username.setText("Email");
                            radiobutton_mobilenumber.setChecked(false);
                            inputlayout_forgotpassword_email.setError(" ");
                            edittext_forgotpassword_email.setText("");
                            textViewone.setVisibility(View.VISIBLE);
                            textViewtwo.setVisibility(View.VISIBLE);
                            textViewthree.setVisibility(View.VISIBLE);
                            edittext_forgotpassword_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                            edittext_forgotpassword_email.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else {
                            spinner_countrycode.setVisibility(View.VISIBLE);
                            //textview_countrycode.setVisibility(View.VISIBLE);
                            textview_username.setText("Mobilenumber");
                        }
                        break;
                }

            }
        });

        btn_verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeybord();
                if (doValidate()) {

                    int id = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(id);

                    if (radioButton.getText().toString().equals("Mobile Number")) {
                        LoginUser = textview_countrycode.getText().toString().trim() + edittext_forgotpassword_email.getText().toString().trim();
                        CheckOTP();
                        //textview_forgot_password.setVisibility(View.GONE);
                    } else {
                        LoginUser = edittext_forgotpassword_email.getText().toString().trim();
                        //textview_forgot_password.setVisibility(View.VISIBLE);
                        ForgotPassword();
                    }

                }
            }
        });

        getCountryList();

        spinner_countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem = parent.getItemAtPosition(position).toString();
                CountryItem = arrayListSpinnerCountry.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* btn_verify_email.setOnClickListener(new View.OnClickListener() {
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
        });*/

        return view;
    }

    private void CheckOTP() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().CheckuserForgot(LoginUser).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.isSuccessful()) {
                    closeKeybord();
                    if (response.body().Code == 200) {
                        user.Mobile = CountryItem + edittext_forgotpassword_email.getText().toString();
                        user.CustomerId = response.body().Item.CustomerId;
                        user.Otp = response.body().Item.Otp;
                        //Toast.makeText(getContext(), "Your OTP is : " + user.Otp, Toast.LENGTH_LONG).show();
                        replaceFragment(new OTPConfirmFragment(user, true), null);
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

            }
        });
    }

    private void ForgotPassword() {
        if (doValidate()) {
            if (!isNetworkConnected()) {
                Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            LoadingDialog.showLoadingDialog(getContext());
            getApiClient().Forgotpassword(LoginUser).enqueue(new Callback<Response<User>>() {
                @Override
                public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                    if (response.code() == Constants.SuccessCode) {
                        if (response.body().Item != null) {
                            showMessage("Forgot Password Successfull");
                            if (radiobutton_mobilenumber.isChecked()) {
                                replaceFragment(new OTPConfirmFragment(user, true), null);
                                textview_forgot_password.setVisibility(View.GONE);
                            } else {
                                textview_forgot_password.setVisibility(View.VISIBLE);
                            }
                            //textview_forgot_password.setVisibility(View.GONE);
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

    private boolean doValidate() {
        if (radiobutton_mobilenumber.isChecked()) {
            if (edittext_forgotpassword_email.getText().toString().trim().isEmpty()) {
                inputlayout_forgotpassword_email.setError("Please enter Mobile Number");
                edittext_forgotpassword_email.requestFocus();
                return false;
            } else {
                edittext_forgotpassword_email.clearFocus();
                inputlayout_forgotpassword_email.setErrorEnabled(false);
            }
        } else {
            if (edittext_forgotpassword_email.getText().toString().trim().isEmpty()) {
                inputlayout_forgotpassword_email.setError("Please enter email");
                edittext_forgotpassword_email.requestFocus();
                return false;
            } else {
                edittext_forgotpassword_email.clearFocus();
                inputlayout_forgotpassword_email.setErrorEnabled(false);
            }
        }

        return true;
    }

    private void getCountryList() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAllCountry().enqueue(new Callback<ListResponse<CountyMaster>>() {
            @Override
            public void onResponse(Call<ListResponse<CountyMaster>> call, retrofit2.Response<ListResponse<CountyMaster>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (!response.body().Values.isEmpty()) {
                        countyMasterList = response.body().Values;

                        if (!arrayListSpinnerCountry.isEmpty())
                            arrayListSpinnerCountry.clear();

                        for (int i = 0; i < countyMasterList.size(); i++) {
                            arrayListSpinnerCountry.add("+"+countyMasterList.get(i).CountryCode);
                        }

                        ArrayAdapter<String> arrayAdapterCountry = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinnerCountry);
                        spinner_countrycode.setAdapter(arrayAdapterCountry);

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
            public void onFailure(Call<ListResponse<CountyMaster>> call, Throwable t) {

            }
        });

    }

}
