package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.plexoc.myapplication.Adapter.PlansAdpter;
import com.plexoc.myapplication.Adapter.RaisedSOSAdpter;
import com.plexoc.myapplication.Adapter.TabAdpter;
import com.plexoc.myapplication.Model.Address;
import com.plexoc.myapplication.Model.ListResponse;
import com.plexoc.myapplication.Model.Plan;
import com.plexoc.myapplication.Model.RaisedSOSUser;
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
public class AddressDetailFragment extends BaseFragment {

    private Toolbar toolbar;
    private RecyclerView recyclerViewRaisedSOS;
    private RaisedSOSAdpter raisedSOSAdpter;
    private List<RaisedSOSUser> raisedSOSUserList = new ArrayList<>();
    private AppCompatTextView textview_norecordfound, textview_address;

    private String name, Address;
    private int AddressId;
    private int raisedSOSID;

    public AddressDetailFragment(String name, String Address, int AddressId) {
        this.name = name;
        this.Address = Address;
        this.AddressId = AddressId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_detail, container, false);


        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);
        textview_address = view.findViewById(R.id.textview_address);
        textview_address.setText(Address);

        recyclerViewRaisedSOS = view.findViewById(R.id.recyclerview_raised_sos);



       /* ViewPager viewPager = view.findViewById(R.id.viewpager);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);

        TabAdpter tabAdpter = new TabAdpter(getChildFragmentManager());
        //tabAdpter.addFragment(new TranscationHistoryFragment(), "Transcations");
        tabAdpter.addFragment(new RaisedSOSFragment(), "Raised Sos  ");

        AppCompatTextView textViewAddress = view.findViewById(R.id.textview_address);
        textViewAddress.setText(Address);

        viewPager.setAdapter(tabAdpter);
        tabLayout.setupWithViewPager(viewPager);*/

        getRaisedSOSDetails();

        return view;
    }

    private void getRaisedSOSDetails() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getAddressWiseRaisedSOS(user.Id, AddressId, 0, 100000).enqueue(new Callback<ListResponse<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<ListResponse<RaisedSOSUser>> call, Response<ListResponse<RaisedSOSUser>> response) {
                if (response.code() == 200) {
                    if (!response.body().Values.isEmpty()) {

                        raisedSOSAdpter = new RaisedSOSAdpter(response.body().Values, new RaisedSOSAdpter.RaisedSOSCallback() {
                            @Override
                            public void getRaisedSOSId(RaisedSOSUser raisedSOSUser) {
                                raisedSOSID = raisedSOSUser.Id;
                                replaceFragment(new RaisedSOSDetailsFragment(raisedSOSID),null);
                            }
                        });
                        recyclerViewRaisedSOS.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewRaisedSOS.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewRaisedSOS.setAdapter(raisedSOSAdpter);

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
            public void onFailure(Call<ListResponse<RaisedSOSUser>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("RaisedSOS Erro : ", t.getLocalizedMessage());
            }
        });
    }

}
