package com.plexoc.mywatchman.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.plexoc.mywatchman.Activity.PaymentActivity;
import com.plexoc.mywatchman.Model.Address;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.OptionsModel;
import com.plexoc.mywatchman.Model.QuestionModel;
import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.Model.SosType;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.DrawerUtil;
import com.plexoc.mywatchman.Utils.LoadingDialog;
import com.plexoc.mywatchman.Utils.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseFragment {


    public DashboardFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private MaterialButton btn_view, btn_view_ongoingsos;
    private RaisedSOSUser raisedSOSUser;
    private String Lat, Long;

    private int sosTypeId;
    private ArrayList<String> arrayListSpinner = new ArrayList<>();
    private List<SosType> sosTypeList = new ArrayList<>();
    private AppCompatSpinner spinner_selectsostype;

    private AppCompatTextView textview_title, textview_question, textview_title_location;
    private MaterialButton button_ok, button_cancel;

    private ConstraintLayout cardview_one, cardview_two, cardview_three, cardview_four;

    private ConstraintLayout constraint_layout_two;
    private ConstraintLayout constraint_layout_three;
    private ConstraintLayout constraint_layout_four;
    private ConstraintLayout constraintLayout_pendind_action, constraint_layout_ongoing_sos;
    private AppCompatTextView textviewLocationOne, textviewLocationTwo, textviewLocationThree, textviewLocationFour;

    private List<Address> addressList = new ArrayList<>();

    private int PERMISSION_ID = 44;
    private int SOSID;
    private boolean canRaiseSos = true;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;


    private List<QuestionModel> questionModelList = new ArrayList<>();

    private List<OptionsModel> optionsModelListone = new ArrayList<>();
    private List<OptionsModel> optionsModelListtwo = new ArrayList<>();
    private List<OptionsModel> optionsModelListthree = new ArrayList<>();
    private List<OptionsModel> optionsModelListfour = new ArrayList<>();
    private List<OptionsModel> optionsModelListfive = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        btn_view = view.findViewById(R.id.btn_view);
        btn_view_ongoingsos = view.findViewById(R.id.btn_view_ongoingsos);
        RecyclerView recyclerViewAddress = view.findViewById(R.id.recyclerview_addresses);

        ConstraintLayout constraint_layout_one = view.findViewById(R.id.constraint_layout_one);
        constraint_layout_two = view.findViewById(R.id.constraint_layout_two);
        constraint_layout_three = view.findViewById(R.id.constraint_layout_three);
        constraint_layout_four = view.findViewById(R.id.constraint_layout_four);

        cardview_one = view.findViewById(R.id.cardview_one);
        cardview_two = view.findViewById(R.id.cardview_two);
        cardview_three = view.findViewById(R.id.cardview_three);
        cardview_four = view.findViewById(R.id.cardview_four);

        constraintLayout_pendind_action = view.findViewById(R.id.constraint_layout_pending_sos);
        constraint_layout_ongoing_sos = view.findViewById(R.id.constraint_layout_ongoing_sos);

        textviewLocationOne = view.findViewById(R.id.textview_location_one);
        textviewLocationTwo = view.findViewById(R.id.textview_location_two);
        textviewLocationThree = view.findViewById(R.id.textview_location_three);
        textviewLocationFour = view.findViewById(R.id.textview_location_four);

        raisedSOSUser = new RaisedSOSUser();


        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

       /* locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);*/

        constraint_layout_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Log.e("LocationPermission", "Granted");

                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    showLocationDialog();
                                } else {
                                    getLastLocation();
                                    //mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }

                                //getActivity().startActivity(new Intent(getContext(), PaymentActivity.class));

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(getContext(), "Location Permission is needed. Please Grant permission from settings", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        constraint_layout_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialougRaisSOS(addressList.get(0).Id, null, null);
                textview_title_location.setText(addressList.get(0).AddressName);
            }
        });

        constraint_layout_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialougRaisSOS(addressList.get(1).Id, null, null);
                textview_title_location.setText(addressList.get(1).AddressName);
            }
        });

        constraint_layout_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialougRaisSOS(addressList.get(2).Id, null, null);
                textview_title_location.setText(addressList.get(2).AddressName);
            }
        });


        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                replaceFragment(new QuestionFragment(questionModelList, SOSID), null);
            }
        });

        btn_view_ongoingsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RoamingStaffIdentityFragment(SOSID), null);
            }
        });

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.e("LocationPermission", "Granted");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Location Permission is needed. Please Grant permission from settings", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        getAddress();

        getCustomer();

        checkActiveSOSofUser();

        getAllSOSType();

        return view;
    }

    private void getCustomer() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getCustomerByid(user.Id).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<User>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<User>> call, Response<com.plexoc.mywatchman.Model.Response<User>> response) {
                if (response.isSuccessful()) {
                    if (response.body().Item != null) {

                        Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                        user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);

                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("User Fail", t.getLocalizedMessage());
            }
        });
    }

    private void getAddress() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAddress(user.Id, 0, 10000).enqueue(new Callback<ListResponse<Address>>() {
            @Override
            public void onResponse(Call<ListResponse<Address>> call, Response<ListResponse<Address>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        addressList = response.body().Values;

                        textviewLocationTwo.setText(response.body().Values.get(0).AddressName);
                        cardview_two.setVisibility(View.VISIBLE);

                        if (response.body().Values.size() >= 2) {
                            textviewLocationThree.setText(response.body().Values.get(1).AddressName);
                            cardview_three.setVisibility(View.VISIBLE);

                            if (response.body().Values.size() >= 3) {
                                textviewLocationFour.setText(response.body().Values.get(2).AddressName);
                                cardview_four.setVisibility(View.VISIBLE);
                            } else {
                                constraint_layout_four.setVisibility(View.GONE);
                                cardview_four.setVisibility(View.GONE);
                            }
                        } else {
                            constraint_layout_three.setVisibility(View.GONE);
                            constraint_layout_four.setVisibility(View.GONE);
                            cardview_three.setVisibility(View.GONE);
                            cardview_four.setVisibility(View.GONE);
                        }

                    } else {
                        constraint_layout_two.setVisibility(View.GONE);
                        constraint_layout_three.setVisibility(View.GONE);
                        constraint_layout_four.setVisibility(View.GONE);
                        cardview_two.setVisibility(View.GONE);
                        cardview_three.setVisibility(View.GONE);
                        cardview_four.setVisibility(View.GONE);
                    }
                } else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<Address>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("GetAddress Fail", t.getLocalizedMessage());
            }
        });
    }

    private void SOSInsert(int AddressID, String Lat, String Long, int sosTypeId) {

        raisedSOSUser.UserId = user.Id;
        raisedSOSUser.AddressId = AddressID;
        raisedSOSUser.Latitude = Lat;
        raisedSOSUser.Longitude = Long;
        raisedSOSUser.SOSTypeId = sosTypeId;

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().RaisedSOSinsert(raisedSOSUser).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> call, Response<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        replaceFragment(new DashboardFragment(), null);
                        Toast.makeText(getContext(), "SOS Raised Successfully", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Address Not Added : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private void DialougRaisSOS(int AddressID, String Lat, String Long) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.raisesos_dialog, viewGroup, false);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        textview_title = view.findViewById(R.id.textview_title);
        textview_question = view.findViewById(R.id.textview_question);
        textview_title_location = view.findViewById(R.id.textview_title_location);
        spinner_selectsostype = view.findViewById(R.id.spinner_selectsostype);
        button_ok = view.findViewById(R.id.button_ok);
        button_cancel = view.findViewById(R.id.button_cancel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrayListSpinner);
        spinner_selectsostype.setAdapter(arrayAdapter);

        spinner_selectsostype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sosTypeId = sosTypeList.get(position).Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (canRaiseSos) {
                    //replaceFragment(new DashboardFragment(), null);
                    //replaceFragment(new RoamingStaffIdentityFragment(), null);
                    SOSInsert(AddressID, Lat, Long, sosTypeId);
                } else {
                    Toast.makeText(getActivity(), "Please complete previously raised sos first before raising another sos", Toast.LENGTH_LONG).show();
                    //replaceFragment(new RoamingStaffIdentityFragment(), null);
                    //Toast.makeText(getContext(), "SOS Raised", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*builder.setTitle("Raise SOS");
        builder.setMessage("Are you sure you want raise SOS for this address");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (canRaiseSos)
                    replaceFragment(new RoamingStaffIdentityFragment(), null);
                    //SOSInsert(AddressID, Lat, Long);
                else
                    Toast.makeText(getActivity(), "Please complete previously raised sos first before raising another sos", Toast.LENGTH_LONG).show();
                //replaceFragment(new RoamingStaffIdentityFragment(), null);
                //Toast.makeText(getContext(), "SOS Raised", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();*/
    }

    private void setData() {

        setOption();
        if (!questionModelList.isEmpty())
            questionModelList.clear();

        QuestionModel questionModel = new QuestionModel(1, "How long did it take the responder to arrive at your SOS location?", false, optionsModelListone, true, true, false);
        questionModelList.add(questionModel);

        questionModel = new QuestionModel(2, "How would you describe your experience?", false, optionsModelListtwo, true, true, false);
        questionModelList.add(questionModel);

        questionModel = new QuestionModel(3, "How would you rate the quality of our SOS services?", false, optionsModelListthree, true, true, false);
        questionModelList.add(questionModel);

        questionModel = new QuestionModel(4, "Overall, how satisfied are you with the services you received from our SOS by Watchman?", false, optionsModelListfour, true, true, false);
        questionModelList.add(questionModel);

        questionModel = new QuestionModel(5, "Would you recommend SOS by Watchman to others?", false, optionsModelListfive, true, true, false);
        questionModelList.add(questionModel);

        questionModel = new QuestionModel(6, "Any other necessary information?", true, null, false, false, false);
        questionModelList.add(questionModel);

        replaceFragment(new QuestionFragment(questionModelList, SOSID), null);
    }

    private void setOption() {
        /*----------------------Options For Question 1----------------------------------------*/

        if (!optionsModelListone.isEmpty())
            optionsModelListone.clear();


        OptionsModel optionsModel = new OptionsModel(1, "0 to 10 Minutes", false, 0, true);
        optionsModelListone.add(optionsModel);

        optionsModel = new OptionsModel(2, "11 to 20 Minutes", false, 0, true);
        optionsModelListone.add(optionsModel);

        optionsModel = new OptionsModel(3, "More than 20 Minutes", false, 0, true);
        optionsModelListone.add(optionsModel);


        /*----------------------Options For Question 2----------------------------------------*/


        if (!optionsModelListtwo.isEmpty())
            optionsModelListtwo.clear();

        optionsModel = new OptionsModel(1, "Excellent", false, 0, true);
        optionsModelListtwo.add(optionsModel);

        optionsModel = new OptionsModel(2, "Good", false, 0, true);
        optionsModelListtwo.add(optionsModel);

        optionsModel = new OptionsModel(3, "Average", false, 0, true);
        optionsModelListtwo.add(optionsModel);

        optionsModel = new OptionsModel(4, "Worse", false, 0, true);
        optionsModelListtwo.add(optionsModel);


        /*----------------------Options For Question 3----------------------------------------*/

        if (!optionsModelListthree.isEmpty())
            optionsModelListthree.clear();

        optionsModel = new OptionsModel(1, "Extremely High Quality", false, 0, true);
        optionsModelListthree.add(optionsModel);

        optionsModel = new OptionsModel(2, "High Quality", false, 0, true);
        optionsModelListthree.add(optionsModel);

        optionsModel = new OptionsModel(3, "Average Quality", false, 0, true);
        optionsModelListthree.add(optionsModel);

        optionsModel = new OptionsModel(4, "Low Quality", false, 0, true);
        optionsModelListthree.add(optionsModel);

        /*----------------------Options For Question 4----------------------------------------*/

        if (!optionsModelListfour.isEmpty())
            optionsModelListfour.clear();

        optionsModel = new OptionsModel(1, "Very Satisfied", false, 0, true);
        optionsModelListfour.add(optionsModel);

        optionsModel = new OptionsModel(2, "Satisfied", false, 0, true);
        optionsModelListfour.add(optionsModel);

        optionsModel = new OptionsModel(3, "Dissatisfied/A", false, 0, true);
        optionsModelListfour.add(optionsModel);

        optionsModel = new OptionsModel(4, "Very dissatisfied", false, 0, true);
        optionsModelListfour.add(optionsModel);

        /*----------------------Options For Question 5----------------------------------------*/

        if (!optionsModelListfive.isEmpty())
            optionsModelListfive.clear();

        optionsModel = new OptionsModel(1, "Yes", false, 0, true);
        optionsModelListfive.add(optionsModel);

        optionsModel = new OptionsModel(2, "No", false, 0, true);
        optionsModelListfive.add(optionsModel);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                task -> {
                    Location location = task.getResult();
                    if (location != null) {
                       /* latTextView.setText(location.getLatitude() + "");
                        lonTextView.setText(location.getLongitude() + "");*/
                        Lat = location.getLatitude() + "";
                        Long = location.getLongitude() + "";
                        DialougRaisSOS(0, Lat, Long);

                        textviewLocationOne.setText("My Location");
                        textview_title_location.setText("My Location");
                    } else {
                        //requestNewLocationData();
                        Toast.makeText(getContext(), "Your Location is null Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        /*locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Lat = location.getLatitude() + "";
                        Long = location.getLongitude() + "";
                        DialougRaisSOS(0, Lat, Long);

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void checkActiveSOSofUser() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        getApiClient().getActiveSOSByUserId(user.Id).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> call, Response<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        canRaiseSos = false;
                        SOSID = response.body().Item.Id;

                        if (response.body().Item.Status == 1) {
                            constraint_layout_ongoing_sos.setVisibility(View.VISIBLE);
                            constraintLayout_pendind_action.setVisibility(View.GONE);
                        }
                        if (response.body().Item.Status == 2) {
                            constraintLayout_pendind_action.setVisibility(View.VISIBLE);
                            constraint_layout_ongoing_sos.setVisibility(View.GONE);
                        }
                    } else {
                        constraintLayout_pendind_action.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<RaisedSOSUser>> call, Throwable t) {
                Log.e("checkactivesosfail", t.getLocalizedMessage());
            }
        });
    }

    private void getAllSOSType() {

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSOSType(0, 10000).enqueue(new Callback<ListResponse<SosType>>() {
            @Override
            public void onResponse(Call<ListResponse<SosType>> call, retrofit2.Response<ListResponse<SosType>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        if (!sosTypeList.isEmpty())
                            sosTypeList.clear();

                        if (!arrayListSpinner.isEmpty())
                            arrayListSpinner.clear();

                        sosTypeList = response.body().Values;

                        for (int i = 0; i < sosTypeList.size(); i++) {
                            arrayListSpinner.add(sosTypeList.get(i).Type);
                        }
                        //Log.d("Response","Success");
                    } else
                        showMessage("No Record Found");
                } else
                    showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<SosType>> call, Throwable t) {
                showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }
        });
    }


    private void showLocationDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Turn On Location");
            builder.setMessage(getResources().getString(R.string.location_permission_msg));
            builder.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    GotoLocation();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GotoLocation() {
        try {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.isFromNotification = false;
    }
}


