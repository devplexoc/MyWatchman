package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.CirclurProgressDrawable;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaisedSOSFragment extends BaseFragment implements OnMapReadyCallback {

    private int SOSID;
    private GoogleMap mMap;
    //private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyC6BHeJdoVNzD2PcBKGqxlFcYcddQFkWD8";
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyDby5O_0c4dCBX50h_8RDYaXuA619oA9lY";

    private Double destinationlat, destinationlong;
    private LatLng dest;

    public RaisedSOSFragment(int SOSID) {
        this.SOSID = SOSID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_raised_so, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("SOS Detail");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            if (Constants.isFromNotification)
                replaceFragment(new DashboardFragment(), DashboardFragment.class.getName());
            else
                getActivity().onBackPressed();
        });

        return view;
    }

    private void getSOSById() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().getSOS(SOSID).enqueue(new Callback<Response<RaisedSOSUser>>() {
            @Override
            public void onResponse(Call<Response<RaisedSOSUser>> call, retrofit2.Response<Response<RaisedSOSUser>> response) {
                if (response.isSuccessful()) {
                    if (response.body().Item != null) {

                        if (response.body().Item.Latitude != null) {
                            destinationlat = Double.parseDouble(response.body().Item.Latitude);
                        }
                        if (response.body().Item.Longitude != null) {
                            destinationlong = Double.parseDouble(response.body().Item.Longitude);
                        }

                        if (destinationlat != null || destinationlong != null) {
                            mMap.clear();
                            dest = new LatLng(destinationlat, destinationlong);
                            mMap.addMarker(new MarkerOptions().position(dest).title(response.body().Item.UserName));
                        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMinZoomPreference(2);
        mMap.setMaxZoomPreference(22);
        mMap.setIndoorEnabled(true);

        getSOSById();

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
    }
}
