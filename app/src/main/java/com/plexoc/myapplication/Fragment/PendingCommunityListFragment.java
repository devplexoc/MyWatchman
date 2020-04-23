package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.plexoc.myapplication.Adapter.ContactAdpter;
import com.plexoc.myapplication.Adapter.PendingContactAdapter;
import com.plexoc.myapplication.Model.EmergencyContact;
import com.plexoc.myapplication.Model.ListResponse;
import com.plexoc.myapplication.Model.PendingContactModel;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Constants;
import com.plexoc.myapplication.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingCommunityListFragment extends BaseFragment {

    private AppCompatTextView textview_norecordfound;
    private RecyclerView recyclerview_pending_contact;

    private List<PendingContactModel> pendingContactModelList = new ArrayList<>();
    private PendingContactAdapter pendingContactAdapter;

    public PendingCommunityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_community_list, container, false);

        recyclerview_pending_contact = view.findViewById(R.id.recyclerview_pending_contact);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);

        getAllEmergencyContact();

        return view;
    }

    private void getAllEmergencyContact() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAllEmergencyContact(user.Id,0,1000000).enqueue(new Callback<ListResponse<EmergencyContact>>() {
            @Override
            public void onResponse(Call<ListResponse<EmergencyContact>> call, Response<ListResponse<EmergencyContact>> response) {
                if(response.isSuccessful()){
                    if(!response.body().Values.isEmpty()){
                        pendingContactAdapter = new PendingContactAdapter(response.body().Values);
                        recyclerview_pending_contact.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerview_pending_contact.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_pending_contact.setAdapter(pendingContactAdapter);

                        textview_norecordfound.setVisibility(View.GONE);
                    }else {
                        textview_norecordfound.setVisibility(View.VISIBLE);
                    }
                }else if (response.code() == Constants.InternalServerError) {
                    showMessage(Constants.DefaultErrorMessage);
                } else {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<EmergencyContact>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("ActiveEmrgContact Fail", t.getLocalizedMessage());
            }
        });
    }


   /* private void setData() {
        PendingContactModel pendingContactModel = new PendingContactModel("Jaymin Prajapati","7405351880");
        pendingContactModelList.add(pendingContactModel);

        pendingContactModel = new PendingContactModel("Chintan Dave","8128445566");
        pendingContactModelList.add(pendingContactModel);

        pendingContactModel = new PendingContactModel("Mayank Prajapati","846042301");
        pendingContactModelList.add(pendingContactModel);

        pendingContactModel = new PendingContactModel("John Karter","9955412211");
        pendingContactModelList.add(pendingContactModel);

        pendingContactModel = new PendingContactModel("John Doe","6359451278");
        pendingContactModelList.add(pendingContactModel);
    }*/


}
