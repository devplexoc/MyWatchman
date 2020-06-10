package com.plexoc.mywatchman.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.plexoc.mywatchman.Activity.HomeActivity;
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
public class SuccessfullySOSCompleteFragment extends BaseFragment {


    public SuccessfullySOSCompleteFragment(int SOSID) {
        this.SOSID = SOSID;
    }

    private int SOSID;
    private MaterialButton btn_home;
    private AppCompatTextView textview_time_min;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_successfully_soscomplete, container, false);

        textview_time_min = view.findViewById(R.id.textview_time_min);
        btn_home = view.findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        CompleteSOS();
        return view;

    }

    private void CompleteSOS() {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().CompleteSOS(SOSID,Constants.Complete).enqueue(new Callback<Response<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<Response<RaisedSOSUser>> call, retrofit2.Response<Response<RaisedSOSUser>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {

                        textview_time_min.setText(response.body().Item.CompletedSOSInMin+" "+"minutes");

                        Log.e("SosCompleted", "Success");
                    } else {
                        Log.e("SosCompleted", "Success");
                    }
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Response<RaisedSOSUser>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("CompleteSosFail", t.getLocalizedMessage());
            }
        });
    }

}
