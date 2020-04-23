package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.plexoc.myapplication.Adapter.ContactAdpter;
import com.plexoc.myapplication.Model.EmergencyContact;
import com.plexoc.myapplication.Model.Error;
import com.plexoc.myapplication.Model.ListResponse;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCommunityFragment extends BaseFragment {


    public MyCommunityFragment() {
        // Required empty public constructor
    }

    private AppCompatTextView textview_norecordfound;
    private RecyclerView recyclerViewAddress;
    private List<EmergencyContact> emergencyContactList = new ArrayList<>();
    private ContactAdpter contactAdpter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_community, container, false);

        recyclerViewAddress = view.findViewById(R.id.recyclerview_emergency_contact);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);

        FloatingActionButton fabAddContact = view.findViewById(R.id.fab_add_contact);

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OpenDialog();
                replaceFragment(new AddEmergencyContactFragment(), null);
            }
        });

        getAllEmergencyContact();

        return view;
    }

    private void getAllEmergencyContact() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAllActiveEmergencyContact(user.Id, 0, 1000000).enqueue(new Callback<ListResponse<EmergencyContact>>() {
            @Override
            public void onResponse(Call<ListResponse<EmergencyContact>> call, Response<ListResponse<EmergencyContact>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {
                        contactAdpter = new ContactAdpter(getContext(), response.body().Values, new ContactAdpter.EmerengencyCallback() {
                            @Override
                            public void getId(int Id) {
                                DeleteContact(Id);
                            }
                        });

                        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewAddress.setAdapter(contactAdpter);
                        textview_norecordfound.setVisibility(View.GONE);
                    } else {
                        recyclerViewAddress.setVisibility(View.GONE);
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
            public void onFailure(Call<ListResponse<EmergencyContact>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("EmergencyContact Fail", t.getLocalizedMessage());
            }
        });
    }

    private void DeleteContact(int id) {

        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().DeleteContact(id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.code() == Constants.SuccessCode) {
                    getAllEmergencyContact();
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                LoadingDialog.cancelLoading();
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

}
