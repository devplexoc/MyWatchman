package com.plexoc.mywatchman.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.plexoc.mywatchman.Activity.PaymentActivity;
import com.plexoc.mywatchman.Adapter.PlansAdpter;
import com.plexoc.mywatchman.Model.Error;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.Plan;
import com.plexoc.mywatchman.Model.PlanDetails;
import com.plexoc.mywatchman.Model.PlanDurationDiscount;
import com.plexoc.mywatchman.Model.TransactionDetails;
import com.plexoc.mywatchman.Model.TransactionHistory;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends BaseFragment {

    private User user;
    private PlanDetails planDetails;

    public PlansFragment(User user) {
        this.user = user;
        // Required empty public constructor
    }

    Toolbar toolbar;
    private MaterialButton btn_make_payment, button_paysecurely;
    MaterialCardView cardvie_payment;
    AppCompatImageView imagevieww_cancel;
    AppCompatTextView textview_norecordfound, textview_planname, textview_plan_amount, textview_multiplication,textview_chooseoption;
    RadioGroup radiogroup;
    RadioButton radioButtonMobileWallete, radioButtonVisa;
    AppCompatCheckBox checkbox_tns;
    boolean isFreePlan;

    private ArrayList<String> arrayListSpinner = new ArrayList<>();
    private List<PlanDurationDiscount> planDurationDiscountList = new ArrayList<>();
    private AppCompatSpinner spinner_selectplanduration;

    List<Plan> planList = new ArrayList<>();
    RecyclerView recyclerViewPlans;
    PlansAdpter plansAdpter;
    boolean selectedplan = false;

    private int planId, durationID;
    private String planName;
    private Float planPrice, totalPlanCalculation;
    private int planMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plans, container, false);

        planDetails = new PlanDetails();

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Select Plan");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        btn_make_payment = view.findViewById(R.id.btn_make_payment);

        recyclerViewPlans = view.findViewById(R.id.recyclerview_plans);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);

        checkbox_tns = view.findViewById(R.id.checkbox_tns);

        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedplan) {
                    if (checkbox_tns.isChecked()) {
                        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialoug, null);
                        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.Dialog);
                        dialog.setContentView(view);
                        dialog.show();

                        textview_planname = view.findViewById(R.id.textview_planname);
                        textview_plan_amount = view.findViewById(R.id.textview_plan_amount);
                        textview_multiplication = view.findViewById(R.id.textview_multiplication);
                        textview_chooseoption = view.findViewById(R.id.textview_chooseoption);
                        radiogroup = view.findViewById(R.id.radiogroup);
                        button_paysecurely = view.findViewById(R.id.button_paysecurely);

                        textview_planname.setText(planName + " " + "PLAN");
                        spinner_selectplanduration = view.findViewById(R.id.spinner_selectplanduration);


                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinner);
                        spinner_selectplanduration.setAdapter(arrayAdapter);

                        spinner_selectplanduration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                durationID = planDurationDiscountList.get(position).Id;

                                if (durationID == 3) {
                                    planMonth = 1;
                                }
                                if (durationID == 2) {
                                    planMonth = 6;
                                }
                                if (durationID == 1) {
                                    planMonth = 12;
                                }

                                totalPlanCalculation = planPrice * planMonth;
                                textview_plan_amount.setText(String.valueOf(totalPlanCalculation));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        if (planName.equals("Promo Plan (Free)")) {
                            durationID = 3;
                            spinner_selectplanduration.setVisibility(View.GONE);
                            textview_plan_amount.setText("0");
                            textview_multiplication.setVisibility(View.GONE);
                        } else {
                            spinner_selectplanduration.setVisibility(View.VISIBLE);
                        }

                        if(planPrice > 0){
                            isFreePlan = false;
                            spinner_selectplanduration.setEnabled(true);

                        }else {
                            isFreePlan = true;
                            spinner_selectplanduration.setSelection(0);
                            textview_chooseoption.setVisibility(View.GONE);
                            spinner_selectplanduration.setEnabled(false);
                            radiogroup.setVisibility(View.GONE);
                            button_paysecurely.setText("Submit");
                            durationID = 3;
                        }

                        button_paysecurely.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                PlanInsert();
                                GetPlanDetails();
                            }
                        });

                        radioButtonMobileWallete = view.findViewById(R.id.radiobutton_mobilewallete);
                        radioButtonVisa = view.findViewById(R.id.radiobutton_visa);
                        radiogroup = view.findViewById(R.id.radiogroup);

                        radioButtonMobileWallete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (radioButtonMobileWallete.isChecked()) {
                                    radioButtonVisa.setChecked(false);
                                }
                            }
                        });

                        radioButtonVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (radioButtonVisa.isChecked()) {
                                    radioButtonMobileWallete.setChecked(false);
                                }
                            }
                        });

                        imagevieww_cancel = view.findViewById(R.id.imagevieww_cancel);
                        imagevieww_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        ConstraintLayout constraintLayout = view.findViewById(R.id.constraint_layout);
                        constraintLayout.setBackgroundResource(android.R.color.transparent);
                        cardvie_payment = view.findViewById(R.id.cardvie_payment);
                        cardvie_payment.setBackgroundResource(R.drawable.corner_radious);
                    } else {
                        Toast.makeText(getContext(), "Please Check Terms and Condition", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please Select Any Plan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getAllPlan();

        getPlanDurationAndDiscount();

        return view;
    }

    private void getAllPlan() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getPlan(0, 1000000).enqueue(new Callback<ListResponse<Plan>>() {
            @Override
            public void onResponse(Call<ListResponse<Plan>> call, Response<ListResponse<Plan>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        plansAdpter = new PlansAdpter(getContext(), response.body().Values, new PlansAdpter.PlanCallBack() {
                            @Override
                            public void getPlanId(Plan plan) {

                                selectedplan = true;
                                planId = plan.Id;
                                planName = plan.PlanCategory;
                                planPrice = (float) plan.Price;

                            }
                        }, true, user.PlanId);

                        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewPlans.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewPlans.setAdapter(plansAdpter);

                        textview_norecordfound.setVisibility(View.GONE);
                    } else {
                        textview_norecordfound.setVisibility(View.VISIBLE);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<Plan>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Plan Not Show", t.getLocalizedMessage());
            }
        });
    }

    private void PlanInsert() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().PlanInsert(user.Id, planId, durationID).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<TransactionHistory>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<TransactionHistory>> call, Response<com.plexoc.mywatchman.Model.Response<TransactionHistory>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                       /* Prefs.putString(Prefs.USER, new Gson().toJson(user));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);*/
                        //replaceFragment(new PaymentSuccessfullFragment(), null);
                        //replaceFragment(new PaymentFragment(), null);
                        //startActivity(new Intent(getContext(), PaymentActivity.class));
                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<TransactionHistory>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Plan Not Added : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private void GetPlanDetails(){
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getPlanDetails(planId,durationID).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<PlanDetails>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<PlanDetails>> call, Response<com.plexoc.mywatchman.Model.Response<PlanDetails>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        planDetails = response.body().Item;
                        if(isFreePlan){
                            TransactionDetails();
                        }else {
                            replaceFragment(new PaymentFragment(user,planDetails), null);
                        }

                    } else {
                        showMessage(response.body().Message);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<PlanDetails>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Plan details not found", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private void getPlanDurationAndDiscount() {

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getPlandurationDiscount(0, 10000).enqueue(new Callback<ListResponse<PlanDurationDiscount>>() {
            @Override
            public void onResponse(Call<ListResponse<PlanDurationDiscount>> call, retrofit2.Response<ListResponse<PlanDurationDiscount>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        if (!planDurationDiscountList.isEmpty())
                            planDurationDiscountList.clear();

                        if (!arrayListSpinner.isEmpty())
                            arrayListSpinner.clear();

                        planDurationDiscountList = response.body().Values;

                        for (int i = 0; i < planDurationDiscountList.size(); i++) {
                            arrayListSpinner.add(planDurationDiscountList.get(i).Name);
                        }
                       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinner);
                        spinner_selectplanduration.setAdapter(arrayAdapter);*/
                        //Log.d("Response","Success");
                    } else
                        showMessage("No Record Found");
                } else
                    showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<PlanDurationDiscount>> call, Throwable t) {
                showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }
        });
    }


    private void TransactionDetails() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().TransactionDetailsUpsert(user.Id,planDetails.Id,null,
                null,null,planDetails.Price,
                planDetails.TransitionDate,planDetails.ExpiryDate).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<TransactionDetails>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<TransactionDetails>> call, retrofit2.Response<com.plexoc.mywatchman.Model.Response<TransactionDetails>> response) {
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
}
