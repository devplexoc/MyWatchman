package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaisedSOSDetailsFragment extends BaseFragment {

    private Toolbar toolbar;
    private AppCompatTextView textview_roaming_staff_name, textview_roaming_staff_age, textview_roaming_staff_rate, textview_sos_date,
            textview_sos_time, textview_sos_comletedtime, textViewSOSType;
    private int raisedSOSID;
    private AppCompatRatingBar ratingBarOverallRating;
    private AppCompatRatingBar ratingBarSOSRating;
    private AppCompatTextView textViewYourRating;

    RaisedSOSDetailsFragment(int raisedSOSID) {
        this.raisedSOSID = raisedSOSID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_raised_sosdetails, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Raised SOS Details");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        textview_roaming_staff_name = view.findViewById(R.id.textview_roaming_staff_name);
        textview_roaming_staff_age = view.findViewById(R.id.textview_roaming_staff_age);
//        textview_roaming_staff_rate = view.findViewById(R.id.textview_roaming_staff_rate);
        textview_sos_date = view.findViewById(R.id.textview_sos_date);
        textview_sos_time = view.findViewById(R.id.textview_sos_time);
        textview_sos_comletedtime = view.findViewById(R.id.textview_sos_comletedtime);
        textViewSOSType = view.findViewById(R.id.textview_sos_type);

        ratingBarOverallRating = view.findViewById(R.id.ratingbar_roaming_staff_rating);
        ratingBarSOSRating = view.findViewById(R.id.roaming_staff_rating);

        textViewYourRating = view.findViewById(R.id.textview_your_rating);

        getSOSById();

        return view;
    }

    private void getSOSById() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSOS(raisedSOSID).enqueue(new Callback<Response<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<Response<RaisedSOSUser>> call, retrofit2.Response<Response<RaisedSOSUser>> response) {
                if (response.isSuccessful()) {
                    if (response.body().Item != null) {

                        if (response.body().Item.ResponserName == null) {
                            textview_roaming_staff_name.setText(" - ");
                            textview_roaming_staff_age.setText(" - ");
                        } else {
                            textview_roaming_staff_name.setText(response.body().Item.ResponserName);
                        }

                        ratingBarOverallRating.setRating(response.body().Item.Rating);

                        if (response.body().Item.CustomerRating != null) {
                            ratingBarSOSRating.setRating(Float.parseFloat(response.body().Item.CustomerRating));
                        } else {
                            ratingBarSOSRating.setVisibility(View.GONE);
                            textViewYourRating.setVisibility(View.GONE);
                        }

                        if (response.body().Item.StartDate == null) {
                            textview_sos_date.setText(" - ");
                        } else {
                            textview_sos_date.setText(response.body().Item.StartDate);
                        }

                        if (response.body().Item.StartTime == null) {
                            textview_sos_time.setText(" - ");
                        } else {
                            textview_sos_time.setText(response.body().Item.StartTime);
                        }


                        if (response.body().Item.CompletedSOSInMin == null) {
                            textview_sos_comletedtime.setText(" - ");
                        } else {
                            textview_sos_comletedtime.setText(response.body().Item.CompletedSOSInMin + " Min.");
                        }

                        if (response.body().Item.SOSType != null)
                            textViewSOSType.setText(response.body().Item.SOSType);
                        else
                            textViewSOSType.setText(" - ");
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
            public void onFailure(Call<Response<RaisedSOSUser>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("RoamingDetailsnotAdd : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }
}


