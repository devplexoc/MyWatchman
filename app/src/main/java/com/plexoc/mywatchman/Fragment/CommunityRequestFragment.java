package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.plexoc.mywatchman.Adapter.CommunityRequestAdpter;
import com.plexoc.mywatchman.Model.EmergencyContact;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.User;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityRequestFragment extends BaseFragment {

    private AppCompatTextView textview_norecordfound;
    private List<User> communityList = new ArrayList<>();
    RecyclerView recyclerViewRequest;
    CommunityRequestAdpter requestAdpter;

    private SwipeRefreshLayout swipeRefreshLayout;

    public CommunityRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_request, container, false);

        recyclerViewRequest = view.findViewById(R.id.recyclerview_community_request);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);

        swipeRefreshLayout = view.findViewById(R.id.swipetorefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllCommunityRequest();
            swipeRefreshLayout.setRefreshing(false);
        });

        //setData();
        getAllCommunityRequest();


        return view;
    }

   /* private void setData() {
        if(communityList != null){
            communityList.clear();
        }
        communityList.add(new Community("Willette Gbelee"));
        communityList.add(new Community("Samuel Richards"));
        communityList.add(new Community("Daniel Cooper"));
    }*/


    private void getAllCommunityRequest() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getCommunityRequest(0, 100000, user.Mobile, user.Email).enqueue(new Callback<ListResponse<User>>() {
            @Override
            public void onResponse(Call<ListResponse<User>> call, Response<ListResponse<User>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        EmergencyContactFragment.setBadgeCount(response.body().Values.size());
                        requestAdpter = new CommunityRequestAdpter(response.body().Values, new CommunityRequestAdpter.Callback() {
                            @Override
                            public void getStatus(int Id, boolean status) {
                                StatusUpdate(Id, status);
                            }
                        }, getContext());
                        recyclerViewRequest.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewRequest.setAdapter(requestAdpter);

                        textview_norecordfound.setVisibility(View.GONE);
                    } else {
                        recyclerViewRequest.setVisibility(View.GONE);
                        EmergencyContactFragment.removeBadgeCount();
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
            public void onFailure(Call<ListResponse<User>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("CommRequestFragment : ", t.getLocalizedMessage());
            }
        });
    }

    private void StatusUpdate(int Id, boolean status) {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().CommunityRequestAproveReject(Id, status).enqueue(new Callback<com.plexoc.mywatchman.Model.Response<EmergencyContact>>() {
            @Override
            public void onResponse(Call<com.plexoc.mywatchman.Model.Response<EmergencyContact>> call, Response<com.plexoc.mywatchman.Model.Response<EmergencyContact>> response) {
                if (response.code() == Constants.SuccessCode) {
                    getAllCommunityRequest();
                } else {
                    showMessage(response.body().Message);
                }

                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<com.plexoc.mywatchman.Model.Response<EmergencyContact>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("RequestError :", t.getLocalizedMessage());
            }
        });
    }

}
