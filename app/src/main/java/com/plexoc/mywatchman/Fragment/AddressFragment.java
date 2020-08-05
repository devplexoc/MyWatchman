package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.plexoc.mywatchman.Adapter.AddressAdpter;
import com.plexoc.mywatchman.Model.Address;
import com.plexoc.mywatchman.Model.ListResponse;
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
public class AddressFragment extends BaseFragment {


    public AddressFragment() {
        // Required empty public constructor
    }

    private AppCompatTextView textview_norecordfound;
    private List<Address> addressList = new ArrayList<>();
    private RecyclerView recyclerViewAddress;
    private AddressAdpter addressAdpter;
    private FloatingActionButton fab_add_address;
    private int Addresscount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("My Address");
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        DrawerUtil.getDrawer(getActivity(), toolbar, user);

        //setData();

        recyclerViewAddress = view.findViewById(R.id.recyclerview_addresses);
        textview_norecordfound = view.findViewById(R.id.textview_norecordfound);
        fab_add_address = view.findViewById(R.id.fab_add_address);

        fab_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replaceFragment(new AddAddressFragment(), null);
                if (user.AddressCount > Addresscount)
                    replaceFragment(new AddAddressFragment(null,false), null);
                else
                    Toast.makeText(getContext(), "You can add only " + user.AddressCount + " address with this plan", Toast.LENGTH_SHORT).show();
            }
        });

        getAddress();
        return view;
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
                        Addresscount = response.body().Values.size();

                        addressAdpter = new AddressAdpter(getContext(),response.body().Values, new AddressAdpter.AddressCallBack() {
                            @Override
                            public void getAddress(String name, String Address, int AddressId) {
                                replaceFragment(new AddressDetailFragment(name, Address, AddressId), null);
                            }
                        },address -> {
                            replaceFragment(new AddAddressFragment(address,true), null);
                        });

                        recyclerViewAddress.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerViewAddress.setAdapter(addressAdpter);
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
            public void onFailure(Call<ListResponse<Address>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("GetAddress Fail", t.getLocalizedMessage());
            }
        });
    }

  /*  private void setData() {
        if (!addressList.isEmpty())
            addressList.clear();*/

        /*addressList.add(new Address("Oliver Wleh Klark, Jr.", "Ethelda James Compound,Oldest Congo Town , Monrovia, Liberia"));
        addressList.add(new Address("Oliver Wleh Klark, Jr.", "Gaye Town Old Road, Sinkor,Monrovia, Liberia"));
        addressList.add(new Address("Oliver Wleh Klark, Jr.", "New Kru Town,Bushrod Island,Monrovia, Liberia"));*/
        /*addressList.add(new Address("James Roberts", "Cowfield Du Port Road, Paynesville, Liberia"));
        addressList.add(new Address("Caroline Harris", "New Matadi Estate, Sinkor, Monrovia, Liberia"));
        addressList.add(new Address("Laurine Richards", "Carey Street, Monrovia, Liberia"));*/
}

