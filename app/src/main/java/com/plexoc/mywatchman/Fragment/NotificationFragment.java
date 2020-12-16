package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.plexoc.mywatchman.Adapter.NotificationAdapter;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.Notifiaction;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.DrawerUtil;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment {


    public NotificationFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;
    private SwipeRefreshLayout swipetorefresh;

    List<String> notificationlist = new ArrayList<>();
    RecyclerView recyclerview_notification;
    NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        swipetorefresh = view.findViewById(R.id.swipetorefresh);
        swipetorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipetorefresh.setRefreshing(false);
            }
        });

        recyclerview_notification = view.findViewById(R.id.recyclerview_notification);

        getNotificationList();
        return view;
    }

    private void getNotificationList() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getNotificationList(user.Id, 0, 10000).enqueue(new Callback<ListResponse<Notifiaction>>() {
            @Override
            public void onResponse(Call<ListResponse<Notifiaction>> call, Response<ListResponse<Notifiaction>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (!response.body().Values.isEmpty()) {
                        notificationAdapter = new NotificationAdapter(getContext(), response.body().Values, id -> {
                            if (id != 0) {
                                replaceFragment(new RaisedSOSFragment(id), RaisedSOSFragment.class.getName());
                            }
                        });
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerview_notification.setLayoutManager(layoutManager);
                        recyclerview_notification.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_notification.setAdapter(notificationAdapter);
                    } else {
                        showMessage("No record Found");
                        recyclerview_notification.setVisibility(View.GONE);
                    }
                } else
                    showMessage(Constants.DefaultErrorMessage);
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<ListResponse<Notifiaction>> call, Throwable t) {

            }
        });
    }
}
