package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
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
public class AddUserFormFragment extends BaseFragment {

    int id = 0;
    private String fName, lName, Email, mobileNo, password;
    Toolbar toolbar;
    private MaterialButton button_submit;
    String str = "+213";
    int nbr = 4;

    private ArrayList<String> arrayListSpinnerCountry = new ArrayList<>();
    private List<CountyMaster> countyMasterList;

    private TextInputLayout textinput_user_name, textinput_user_lastname, textinput_user_mobilenumber, textinput_user_password, textinput_user_email;
    private TextInputEditText edittext_user_name, edittext_user_lastname, edittext_user_mobilenumber, edittext_user_password,
            edittext_user_email,edittext_countrycode;
    private AppCompatTextView textview_user_password, textview_countrycode;
    private AppCompatImageView imageview_contact;
    private AppCompatSpinner spinner_countrycode;
    private String CountryItem;

    private MaterialButton button_signup;

    public AddUserFormFragment(int id, String fName, String lName, String Email, String mobileNo, String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.Email = Email;
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
        textinput_user_email = view.findViewById(R.id.textinput_addcontact_contectemail);

        edittext_user_name = view.findViewById(R.id.edittext_user_name);
        edittext_user_lastname = view.findViewById(R.id.edittext_user_lastname);
        edittext_user_mobilenumber = view.findViewById(R.id.edittext_user_mobilenumber);
        edittext_user_password = view.findViewById(R.id.edittext_user_password);
        edittext_user_email = view.findViewById(R.id.edittext_addcontact_contectemail);
        edittext_countrycode = view.findViewById(R.id.edittext_countrycode);

        spinner_countrycode = view.findViewById(R.id.spinner_countrycode);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Users");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());


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

        if (fName != null) {
            edittext_user_name.setText(fName);
        }
        if (lName != null) {
            edittext_user_lastname.setText(lName);
        }
        if (Email != null) {
            edittext_user_email.setEnabled(false);
            edittext_user_email.setText(Email);
        }
        if (mobileNo != null) {
            edittext_user_mobilenumber.setEnabled(false);
            /* String mobile = "";*/
            mobileNo = mobileNo.substring(4);
            edittext_user_mobilenumber.setText(mobileNo);
        }
        if (password != null) {
            edittext_user_password.setText(password);
            textinput_user_password.setVisibility(View.GONE);
            textview_user_password.setVisibility(View.GONE);
            edittext_user_password.setVisibility(View.GONE);
        }

        button_submit = view.findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {

                    user.ParentCustomer = user.Id;
                    user.FirstName = edittext_user_name.getText().toString().trim();
                    user.LastName = edittext_user_lastname.getText().toString().trim();
                    user.Mobile = edittext_countrycode.getText().toString().trim() + edittext_user_mobilenumber.getText().toString().trim();
                    user.Email = edittext_user_email.getText().toString().trim();
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
        getApiClient().SubUserSignup(id, user.ParentCustomer, edittext_user_name.getText().toString().trim(), edittext_user_lastname.getText().toString().trim(),
                edittext_countrycode.getText().toString().trim() + edittext_user_mobilenumber.getText().toString().trim(), edittext_user_email.getText().toString().trim(), edittext_user_password.getText().toString().trim()).enqueue(new Callback<Response<User>>() {
            @Override
            public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        closeKeybord();
                        replaceFragment(new AddUserFragment(), null);

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

        if (password == null) {
            if (edittext_user_password.getText().toString().trim().isEmpty()) {
                textinput_user_password.setError("Please enter password");
                edittext_user_password.requestFocus();
                return false;
            } else {
                edittext_user_password.clearFocus();
                textinput_user_password.setErrorEnabled(false);
            }

            if (!isValidPassword(edittext_user_password.getText().toString().trim())) {
                textinput_user_password.setError("password must be of minimum 8 characters and must include a capital letter , a special charcter and a digit");
                edittext_user_password.requestFocus();
                return false;
            } else {
                textinput_user_password.setErrorEnabled(false);
                edittext_user_password.clearFocus();
            }
        }

        if (!edittext_user_email.getText().toString().trim().isEmpty()) {

            if (!isValidEmail(edittext_user_email.getText().toString().trim())) {
                textinput_user_email.setError("Please enter valid email");
                edittext_user_email.requestFocus();
                return false;
            } else {
                textinput_user_email.setErrorEnabled(false);
                edittext_user_email.clearFocus();
            }
        }

        return true;
    }
}
