package com.plexoc.mywatchman.Fragment;


import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.BuildConfig;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends BaseFragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    private ArrayList<String> arrayListSpinnerCountry = new ArrayList<>();
    private List<CountyMaster> countyMasterList;

    private AppCompatSpinner spinner_countrycode;

    private TextInputLayout textinput_firstname, textinput_lastname, textinput_email, textinput_mobilenumber,
            textinput_password, textinput_username,textinput_countrycode;
    private TextInputEditText edittext_firstname, edittext_lastname, edittext_email, edittext_mobilenumber,
            edittext_password, edittext_username,edittext_countrycode;
    private MaterialButton button_signup;
    private String CountryItem;

    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private AppCompatTextView textviewLogin, textview_countrycode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        //textviewLogin = view.findViewById(R.id.textview_login);
        textview_countrycode = view.findViewById(R.id.textview_countrycode);

        textinput_firstname = view.findViewById(R.id.textinput_firstname);
        textinput_lastname = view.findViewById(R.id.textinput_lastname);
        textinput_email = view.findViewById(R.id.textinput_email);
        textinput_mobilenumber = view.findViewById(R.id.textinput_mobilenumber);
        textinput_password = view.findViewById(R.id.textinput_password);
        textinput_username = view.findViewById(R.id.textinput_username);
        textinput_countrycode = view.findViewById(R.id.textinput_countrycode);

        spinner_countrycode = view.findViewById(R.id.spinner_countrycode);

        edittext_firstname = view.findViewById(R.id.edittext_firstname);
        edittext_lastname = view.findViewById(R.id.edittext_lastname);
        edittext_email = view.findViewById(R.id.edittext_email);
        edittext_mobilenumber = view.findViewById(R.id.edittext_mobilenumber);
        edittext_password = view.findViewById(R.id.edittext_password);
        edittext_username = view.findViewById(R.id.edittext_username);
        edittext_countrycode = view.findViewById(R.id.edittext_countrycode);

        button_signup = view.findViewById(R.id.button_signup);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replaceFragment(new PlansFragment(),null);
                if (doValidate()) {

                    user.Id = 0;
                    user.FirstName = edittext_firstname.getText().toString().trim();
                    user.LastName = edittext_lastname.getText().toString().trim();
                    user.UserName = edittext_username.getText().toString().trim();
                    user.Email = edittext_email.getText().toString().trim();
                    user.Mobile = edittext_countrycode.getText().toString().trim() + edittext_mobilenumber.getText().toString().trim();
                    user.Password = edittext_password.getText().toString().trim();
                    user.DeviceToken = Constants.DeviceToken;
                    user.DeviceInfo = "Android : OS -> " + Build.VERSION.RELEASE + " , Model -> " + Build.MODEL + " , Brand -> " + Build.MODEL + " , App Version ->" + BuildConfig.VERSION_NAME;

                    CheckOTP();

                    //CallSignupApi();
                }
            }
        });

        edittext_countrycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_countrycode.performClick();
            }
        });

        getCountryList();

        spinner_countrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem = parent.getItemAtPosition(position).toString();
                CountryItem = arrayListSpinnerCountry.get(position);

                for (int i = 0; i < countyMasterList.size(); i++) {
                    if (countyMasterList.get(i).Name.equals(CountryItem)) {
                        user.CountryId = countyMasterList.get(i).Id;
                        edittext_countrycode.setText("+" + countyMasterList.get(i).CountryCode);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  textviewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LoginFragment(),null);
            }
        });*/

        return view;
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

                        //replaceFragment(new PlansFragment(response.body().Item),null);

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


    private void CheckOTP() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().Checkuser(edittext_countrycode.getText().toString() + edittext_mobilenumber.getText().toString().trim(),
                edittext_username.getText().toString().trim(),
                edittext_email.getText().toString().trim()).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.isSuccessful()) {
                    closeKeybord();
                    if (response.body().Code == 200) {
                        user.Otp = response.body().Item.Otp;
                       // Toast.makeText(getContext(), "Your OTP is : " + user.Otp, Toast.LENGTH_LONG).show();
                        replaceFragment(new OTPConfirmFragment(user,false), null);
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

    private boolean doValidate() {

        if (edittext_firstname.getText().toString().trim().isEmpty()) {
            textinput_firstname.setError("Please enter first name");
            edittext_firstname.requestFocus();
            return false;
        } else {
            edittext_firstname.clearFocus();
            textinput_firstname.setErrorEnabled(false);
        }

       /* if (edittext_lastname.getText().toString().trim().isEmpty()) {
            textinput_lastname.setError("Please enter last name");
            edittext_lastname.requestFocus();
            return false;
        } else {
            edittext_lastname.clearFocus();
            textinput_lastname.setErrorEnabled(false);
        }*/

        if (edittext_username.getText().toString().trim().isEmpty()) {
            textinput_username.setError("Please enter Username");
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

        if (!isValidPassword(edittext_password.getText().toString().trim())) {
            textinput_password.setError("password must be of minimum 8 characters and must include a capital letter , a special charcter and a digit");
            edittext_password.requestFocus();
            return false;
        }else {
            textinput_password.setErrorEnabled(false);
            edittext_password.clearFocus();
        }


        if (edittext_mobilenumber.getText().toString().trim().isEmpty()) {
            textinput_mobilenumber.setError("Please enter mobile number");
            edittext_mobilenumber.requestFocus();
            return false;
        } else {
            edittext_mobilenumber.clearFocus();
            textinput_mobilenumber.setErrorEnabled(false);
        }

      /*  if (edittext_mobilenumber.getText().toString().trim().length() <= 13) {
            textinput_mobilenumber.setError("Please 13 character mobile number");
            edittext_mobilenumber.requestFocus();
            return false;
        } else {
            edittext_mobilenumber.clearFocus();
            textinput_mobilenumber.setErrorEnabled(false);
        }*/

       /* if (edittext_email.getText().toString().trim().isEmpty()) {
            textinput_email.setError("Please enter Email");
            edittext_email.requestFocus();
            return false;
        } else {
            edittext_email.clearFocus();
            textinput_email.setErrorEnabled(false);
        }
*/
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
                            arrayListSpinnerCountry.add(countyMasterList.get(i).Name);
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
