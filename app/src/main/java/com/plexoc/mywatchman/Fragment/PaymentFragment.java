package com.plexoc.mywatchman.Fragment;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.PlanDetails;
import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.Model.TransactionDetails;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends BaseFragment {

    private User user;
    private PlanDetails planDetails;
    private TransactionDetails transactionDetails;
    private AppCompatTextView txt_plan_details, txt_subscription, txt_total_amount;
    private MaterialButton btn_make_payment;
    private TextInputLayout textinput_card_number,textinput_card_expiry,textinput_card_cvv,textinput_cardholder_name;
    private TextInputEditText edittext_card_number,edittext_card_expiry,edittext_card_cvv,edittext_cardholder_name;
    private int mYear, mMonth, mDay;
    private String day, month;
    int yearSelected;
    int monthSelected;
    String[] months =new String[] {"01","02","03","04","05","06","07","08","09","10","11","12"};

    public PaymentFragment(User user, PlanDetails planDetails) {
        this.user = user;
        this.planDetails = planDetails;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        txt_plan_details = view.findViewById(R.id.txt_plan_details);
        txt_subscription = view.findViewById(R.id.txt_subscription);
        txt_total_amount = view.findViewById(R.id.txt_total_amount);
        btn_make_payment = view.findViewById(R.id.btn_make_payment);

        textinput_card_number = view.findViewById(R.id.textinput_card_number);
        textinput_card_expiry = view.findViewById(R.id.textinput_card_expiry);
        textinput_card_cvv = view.findViewById(R.id.textinput_card_cvv);
        textinput_cardholder_name = view.findViewById(R.id.textinput_cardholder_name);

        edittext_card_number = view.findViewById(R.id.edittext_card_number);
        edittext_card_expiry = view.findViewById(R.id.edittext_card_expiry);
        edittext_card_cvv = view.findViewById(R.id.edittext_card_cvv);
        edittext_cardholder_name = view.findViewById(R.id.edittext_cardholder_name);

        if(planDetails.PlanCategory != null){
            txt_plan_details.setText(planDetails.PlanCategory);
        }if(planDetails.PlanDetails != null){
            txt_subscription.setText(planDetails.PlanDetails);
        }if(planDetails.Price != null){
            txt_total_amount.setText("$" + planDetails.Price);
        }

     /*   cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup*/

        edittext_card_expiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (v == edittext_card_expiry) {
                        /*final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        monthOfYear = monthOfYear + 1;

                                        if (monthOfYear < 10) {

                                            month = "0" + monthOfYear;
                                        } else {
                                            month = String.valueOf(monthOfYear);
                                        }
                                        if (dayOfMonth < 10) {

                                            day = "0" + dayOfMonth;
                                        } else {
                                            day = String.valueOf(dayOfMonth);
                                        }

                                        edittext_card_expiry.setText(day + "/" + (month) + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();*/

                        Calendar calendar = Calendar.getInstance();
                        yearSelected = calendar.get(Calendar.YEAR);
                        monthSelected = calendar.get(Calendar.MONTH);

                        MonthYearPickerDialogFragment dialogFragment = MonthYearPickerDialogFragment
                                .getInstance(monthSelected, yearSelected);

                        dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int year, int monthOfYear) {
                                edittext_card_expiry.setText(year+"-"+months[monthOfYear]);
                            }
                        });


                        dialogFragment.show(getFragmentManager(), null);

                    }


                }
            }
        });

        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doValidate()){
                   /* transactionDetails.CustomerId = user.Id;
                    transactionDetails.PlanId = planDetails.Id;
                    transactionDetails.CardNumber = edittext_card_number.getText().toString().trim();
                    transactionDetails.CardCvv = edittext_card_cvv.getText().toString().trim();
                    transactionDetails.CardMonthYear = edittext_card_expiry.getText().toString().trim();
                    transactionDetails.Price = planDetails.Price;
                    transactionDetails.TransitionDate = planDetails.TransitionDate;
                    transactionDetails.ExpiryDate = planDetails.ExpiryDate;*/

                    TransactionDetails();
                }

            }
        });

        return view;
    }

    private void TransactionDetails() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().TransactionDetailsUpsert(user.Id,planDetails.Id,edittext_card_number.getText().toString().trim(),
                edittext_card_cvv.getText().toString().trim(),edittext_card_expiry.getText().toString().trim(),planDetails.Price,
                planDetails.TransitionDate,planDetails.ExpiryDate).enqueue(new Callback<Response<TransactionDetails>>() {
            @Override
            public void onResponse(Call<Response<TransactionDetails>> call, retrofit2.Response<Response<TransactionDetails>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        Prefs.putString(Prefs.USER, new Gson().toJson(user));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        replaceFragment(new PaymentSuccessfullFragment(),null);

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
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<TransactionDetails>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Address Not Added : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private boolean doValidate() {
        if (edittext_cardholder_name.getText().toString().trim().isEmpty()) {
            textinput_cardholder_name.setError("Please enter card holder name");
            edittext_cardholder_name.requestFocus();
            return false;
        } else {
            edittext_cardholder_name.clearFocus();
            textinput_cardholder_name.setErrorEnabled(false);
        }

        if (edittext_card_number.getText().toString().trim().isEmpty()) {
            textinput_card_number.setError("Please enter card number");
            edittext_card_number.requestFocus();
            return false;
        } else {
            edittext_card_number.clearFocus();
            textinput_card_number.setErrorEnabled(false);
        }

        if(edittext_card_number.getText().length() != 16){
            textinput_card_number.setError("Card number length should be 16");
            edittext_card_number.requestFocus();
            return false;
        }else {
            edittext_card_number.clearFocus();
            textinput_card_number.setErrorEnabled(false);
        }

        if (edittext_card_expiry.getText().toString().trim().isEmpty()) {
            textinput_card_expiry.setError("Please enter expiry date");
            edittext_card_expiry.requestFocus();
            return false;
        } else {
            edittext_card_expiry.clearFocus();
            textinput_card_expiry.setErrorEnabled(false);
        }

        if (edittext_card_cvv.getText().toString().trim().isEmpty()) {
            textinput_card_cvv.setError("Please enter CVV");
            edittext_card_cvv.requestFocus();
            return false;
        } else {
            edittext_card_cvv.clearFocus();
            textinput_card_cvv.setErrorEnabled(false);
        }

        if(edittext_card_cvv.getText().length() != 3){
            textinput_card_cvv.setError("Card CVV length should be 3");
            edittext_card_cvv.requestFocus();
            return false;
        }else {
            edittext_card_cvv.clearFocus();
            textinput_card_cvv.setErrorEnabled(false);
        }


        return true;
    }
}
