package com.plexoc.myapplication.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.plexoc.myapplication.Activity.LoginSignupActivity;
import com.plexoc.myapplication.Adapter.ContactAdpter;
import com.plexoc.myapplication.Adapter.PlansAdpter;
import com.plexoc.myapplication.Adapter.TransctionHistoryAdpter;
import com.plexoc.myapplication.Model.EmergencyContact;
import com.plexoc.myapplication.Model.Error;
import com.plexoc.myapplication.Model.ListResponse;
import com.plexoc.myapplication.Model.Plan;
import com.plexoc.myapplication.Model.PlanDurationDiscount;
import com.plexoc.myapplication.Model.TransactionHistory;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.DrawerUtil;
import com.plexoc.myapplication.Utils.LoadingDialog;
import com.plexoc.myapplication.Utils.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingFragment extends BaseFragment {

    public BillingFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private MaterialButton button_plan_cancel, button_plan_upgrade, button_save;

    private List<TransactionHistory> transactionlist = new ArrayList<>();
    private RecyclerView recyclerViewTransctionHistory, recyclerview_plan;
    private TransctionHistoryAdpter transctionHistoryAdpter;
    private AppCompatTextView textview_norecordfound, textview_planname, textview_plan_amount;

    private ArrayList<String> arrayListSpinner = new ArrayList<>();
    private List<PlanDurationDiscount> planDurationDiscountList = new ArrayList<>();
    private AppCompatSpinner spinner_selectplanduration;

    List<Plan> planList = new ArrayList<>();
    PlansAdpter plansAdpter;
    boolean selectedplan = false;

    private String planName;
    private Float planPrice, totalPlanCalculation;
    private int planMonth;

    private int planId, durationID;

    private AppCompatTextView textview_plan, textview_nxt_trns_date, textview_plan_price, textview_address_count;
    private AppCompatImageView imagevieww_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);
        recyclerViewTransctionHistory = view.findViewById(R.id.recyclerview_transction_history);
        button_plan_cancel = view.findViewById(R.id.button_plan_cancel);
        button_plan_upgrade = view.findViewById(R.id.button_plan_upgrade);


        textview_plan = view.findViewById(R.id.textview_plan);
        textview_nxt_trns_date = view.findViewById(R.id.textview_nxt_trns_date);
        textview_plan_price = view.findViewById(R.id.textview_plan_price);
        textview_address_count = view.findViewById(R.id.textview_address_count);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Billing");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        planId = user.PlanId;
       /* planPrice = (float) user.PlanPrice;
        planName =  user.PlanName;

        if (user.PlanDuration == 3) {
            planMonth = 1;
        }
        if (durationID == 2) {
            planMonth = 12;
        }
        if (durationID == 1) {
            planMonth = 6;
        }*/

        recyclerViewTransctionHistory = view.findViewById(R.id.recyclerview_transction_history);


        //getCustomerCurrentPlan();

        textview_plan.setText(user.PlanName);
        textview_nxt_trns_date.setText(user.PlanEndDate);
        textview_plan_price.setText("$" + " " + String.valueOf(user.PlanPrice));
        textview_address_count.setText(String.valueOf(user.AddressCount));

        button_plan_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure want to Cancel Plan?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CallCancelPlanApi();
                                Intent intent = new Intent(getContext(), LoginSignupActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                //user.Id = user.CustomerId;

            }
        });

        button_plan_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Plan Upgrade Successfully", Toast.LENGTH_SHORT).show();
                getAllPlan();
            }
        });

        getAllTransactionHistory();

        getPlanDurationAndDiscount();

        return view;
    }

    private void getAllTransactionHistory() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getTransactionHistory(user.Id, 0, 1000000).enqueue(new Callback<ListResponse<TransactionHistory>>() {
            @Override
            public void onResponse(Call<ListResponse<TransactionHistory>> call, Response<ListResponse<TransactionHistory>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        transctionHistoryAdpter = new TransctionHistoryAdpter(response.body().Values, getContext());

                        recyclerViewTransctionHistory.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewTransctionHistory.setAdapter(transctionHistoryAdpter);

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
            public void onFailure(Call<ListResponse<TransactionHistory>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("TransactionHistoryErr :", t.getLocalizedMessage());
            }
        });
    }

    private void CallCancelPlanApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().PlanCancel(user.Id).enqueue(new Callback<com.plexoc.myapplication.Model.Response<User>>() {
            @Override
            public void onResponse(Call<com.plexoc.myapplication.Model.Response<User>> call, Response<com.plexoc.myapplication.Model.Response<User>> response) {
                if (response.body().Code == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        user.Id = user.CustomerId;

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                        Toast.makeText(getContext(), "Plan Cancel Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        showMessage(response.body().Message);
                        //Toast.makeText(getContext(), "Plan Not Cancel", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<com.plexoc.myapplication.Model.Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Plan Cancel :", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
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

                        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_plan_dialoug, null);
                        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.Dialog);
                        dialog.setContentView(view);
                        dialog.show();

                        recyclerview_plan = view.findViewById(R.id.recyclerview_plan);
                        textview_planname = view.findViewById(R.id.textview_planname);
                        textview_plan_amount = view.findViewById(R.id.textview_plan_amount);
                        imagevieww_cancel = view.findViewById(R.id.imagevieww_cancel);

                        imagevieww_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });



                        spinner_selectplanduration = view.findViewById(R.id.spinner_selectplanduration);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinner);
                        spinner_selectplanduration.setAdapter(arrayAdapter);

                        spinner_selectplanduration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                durationID = planDurationDiscountList.get(position).Id;

                              /*  if (durationID == 3) {
                                    planMonth = 1;
                                }
                                if (durationID == 2) {
                                    planMonth = 6;
                                }
                                if (durationID == 1) {
                                    planMonth = 12;
                                }

                                totalPlanCalculation = planPrice*planMonth;
                                textview_plan_amount.setText(String.valueOf(totalPlanCalculation));*/
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        button_save = view.findViewById(R.id.button_save);
                        button_save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        plansAdpter = new PlansAdpter(getContext(), response.body().Values, new PlansAdpter.PlanCallBack() {
                            @Override
                            public void getPlanId(Plan plan) {
                               /* selectedplan = true;
                                user.PlanId = plan.Id;*/
                                selectedplan = true;
                                planId = plan.Id;
                                planName = plan.PlanCategory;
                                planPrice = (float) plan.Price;

                                textview_planname.setText(planName + " " + "PLAN");
                            }

                        }, false, planId);

                        recyclerview_plan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        recyclerview_plan.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_plan.setAdapter(plansAdpter);

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
                      /*  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinner);
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
}
