package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plexoc.mywatchman.Adapter.UserAddAdapter;
import com.plexoc.mywatchman.Model.ListResponse;
import com.plexoc.mywatchman.Model.User;
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
public class AddUserFragment extends BaseFragment {


    public AddUserFragment() {
        // Required empty public constructor
    }

    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    private AppCompatTextView textview_norecordfound;

    List<User> userlist = new ArrayList<>();
    RecyclerView recyclerview_useradd;
    UserAddAdapter userAddAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Users");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        floatingActionButton = view.findViewById(R.id.floating_action_useradd);
        recyclerview_useradd = view.findViewById(R.id.recyclerview_useradd);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AddUserFormFragment(0, null, null, null, null, null), null);
            }
        });

        getAllSubUser();

        return view;
    }

    private void getAllSubUser() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAllSubUsers(user.Id, 0, 1000000, null).enqueue(new Callback<ListResponse<User>>() {
            @Override
            public void onResponse(Call<ListResponse<User>> call, Response<ListResponse<User>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().Values.isEmpty()) {

                        userAddAdapter = new UserAddAdapter(response.body().Values, getContext(), (Id, firstName, lastName, Email, mobileNumber, password) -> {
                            replaceFragment(new AddUserFormFragment(Id, firstName, lastName, Email, mobileNumber, password), null);
                        }, Id -> {
                            //Id = user.CustomerId;
                            getSubUserDelete(Id);
                        }, (Id, Password) -> {
                            replaceFragment(new SubUserResetPasswordFragment(Id, Password), null);
                        });

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerview_useradd.setLayoutManager(layoutManager);
                        recyclerview_useradd.setItemAnimator(new DefaultItemAnimator());
                        recyclerview_useradd.setAdapter(userAddAdapter);

                        textview_norecordfound.setVisibility(View.GONE);
                    } else {
                        recyclerview_useradd.setVisibility(View.GONE);
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
                Log.e("SubuserNotShow", t.getLocalizedMessage());
            }
        });
    }

    private void getSubUserDelete(int Id) {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().delete(Id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    if (response.isSuccessful()) {
                        getAllSubUser();
                        Toast.makeText(getContext(), "User Delete Successfull", Toast.LENGTH_SHORT).show();
                    } else {
                        showMessage("Please Check Again User Not Delete Successfull");
                    }
                } catch (Exception e) {
                    showMessage(Constants.DefaultErrorMessage);
                }
                LoadingDialog.cancelLoading();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserNotDelete", t.getLocalizedMessage());
                LoadingDialog.cancelLoading();
            }
        });
    }

}
