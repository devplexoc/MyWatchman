package com.plexoc.mywatchman.Fragment;


import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.plexoc.mywatchman.Model.Address;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */

public class AddAddressFragment extends BaseFragment implements OnMapReadyCallback {

    private Address address;

    public AddAddressFragment() {
        // Required empty public constructor
    }

    private Geocoder geocoder;
    private List<android.location.Address> addresses;

    private TextInputLayout textinput_address_name, textinput_address;
    private TextInputEditText edittext_address_name, edittext_address;
    private String myAddress;
    private Marker marker;
    //String latitude, longtitude;
    private Double latitude, longtitude;
    //String address;

    private MaterialButton buttonAddAddress;

    private MapView mapView;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyC6BHeJdoVNzD2PcBKGqxlFcYcddQFkWD8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);

        address = new Address();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Address");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        textinput_address_name = view.findViewById(R.id.textinput_address_name);
        //textinput_address = view.findViewById(R.id.textinput_address);

        edittext_address_name = view.findViewById(R.id.edittext_address_name);
        //edittext_address = view.findViewById(R.id.edittext_address);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        buttonAddAddress = view.findViewById(R.id.btn_add_address);
        buttonAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doValidate()) {

                    //myAddress = edittext_address.getText().toString().trim();

                   /* GeocodingLocation locaionAddress = new GeocodingLocation();
                    locaionAddress.getAddressFromLocation(myAddress, getContext(), new GeocoderHandler());*/

                    address.CustomerId = user.Id;
                    address.Address = myAddress;
                    address.AddressName = edittext_address_name.getText().toString().trim();
                    address.Latitude = String.valueOf(latitude);
                    address.Longitude = String.valueOf(longtitude);

                    AddCustomerAddressApi();
                    //replaceFragment(new PlansFragment(), null);
                }

            }
        });
        return view;
    }

    private boolean doValidate() {
        if (edittext_address_name.getText().toString().trim().isEmpty()) {
            textinput_address_name.setError("Please enter address name");
            edittext_address_name.requestFocus();
            return false;
        } else {
            edittext_address_name.clearFocus();
            textinput_address_name.setErrorEnabled(false);
        }
       /* if (edittext_address.getText().toString().trim().isEmpty()) {
            textinput_address.setError("Please enter address");
            edittext_address.requestFocus();
            return false;
        } else {
            edittext_address.clearFocus();
            textinput_address.setErrorEnabled(false);
        }*/

        return true;
    }

    private void AddCustomerAddressApi() {
        if (!isNetworkConnected()) {
            Toast.makeText(getContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        LoadingDialog.showLoadingDialog(getContext());
        getApiClient().AddCustomerAddress(address).enqueue(new Callback<Response<Address>>() {
            @Override
            public void onResponse(Call<Response<Address>> call, retrofit2.Response<Response<Address>> response) {
                if (response.code() == Constants.SuccessCode) {
                    if (response.body().Item != null) {
                        Toast.makeText(getContext(), "Address Add Successfully", Toast.LENGTH_SHORT).show();
                        replaceFragment(new AddressFragment(), null);
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
            public void onFailure(Call<Response<Address>> call, Throwable t) {
                LoadingDialog.cancelLoading();
                Log.e("Address Not Added : ", t.getLocalizedMessage());
                showMessage(Constants.DefaultErrorMessage);
            }
        });
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String locationAddress;

            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            if (locationAddress != null) {
                if (!locationAddress.startsWith("Address")) {

                    LatLng latLng = new LatLng(Double.valueOf(locationAddress.split("\n")[0]), Double.valueOf(locationAddress.split("\n")[1]));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng).draggable(true);
                    mMap.addMarker(markerOptions);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                    //Toast.makeText(getContext(), "Address : "+locationAddress, Toast.LENGTH_SHORT).show();
                    Log.e("Lat", locationAddress.split("\n")[0]);
                    Log.e("Long", locationAddress.split("\n")[1]);

                   /* latitude = String.valueOf(latLng.latitude);
                    longtitude = String.valueOf(latLng.longitude);*/

                    latitude = Double.parseDouble(String.valueOf(latLng.latitude));
                    longtitude = Double.parseDouble(String.valueOf(latLng.longitude));

                } else {
                    Toast.makeText(getContext(), locationAddress, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(3);
        mMap.setMaxZoomPreference(20);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
        // mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(4.1555907, -11.6080764), new LatLng(8.5519861, -7.367323)));
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);


        LatLng ny = new LatLng(6.2645484, -10.7206074);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.e("CenterLatLong", mMap.getCameraPosition().target.toString());
                latitude = mMap.getCameraPosition().target.latitude;
                longtitude = mMap.getCameraPosition().target.longitude;
                getAddress();
            }
        });

     /*   mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).draggable(true);
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                *//*latitude = String.valueOf(latLng.latitude);
                longtitude = String.valueOf(latLng.longitude);*//*

                latitude = Double.parseDouble(String.valueOf(latLng.latitude));
                longtitude = Double.parseDouble(String.valueOf(latLng.longitude));
                getAddress();
                buttonAddAddress.setVisibility(View.VISIBLE);

            }
        });
*/
      /*  edittext_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    myAddress = edittext_address.getText().toString().trim();

                    GeocodingLocation locaionAddress = new GeocodingLocation();
                    locaionAddress.getAddressFromLocation(myAddress, getContext(), new GeocoderHandler());
                    closeKeybord();
                }
                return true;
            }
        });*/

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny, 15f));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
               /* Log.e("MarkerDragStart", "Lat" + marker.getPosition().latitude);
                Log.e("MarkerDragStart", "Long" + marker.getPosition().longitude);*/
            }

            @Override
            public void onMarkerDrag(Marker marker) {
               /* Log.e("MarkerDrag", "Lat" + marker.getPosition().latitude);
                Log.e("MarkerDrag", "Long" + marker.getPosition().longitude);*/
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                /*Log.e("MarkerDragEnd", "Lat" + marker.getPosition().latitude);
                Log.e("MarkerDragEnd", "Long" + marker.getPosition().longitude);*/

                /*latitude = String.valueOf(marker.getPosition().latitude);
                longtitude = String.valueOf(marker.getPosition().longitude);*/

                // mMap.clear();

                latitude = Double.parseDouble(String.valueOf(marker.getPosition().latitude));
                longtitude = Double.parseDouble(String.valueOf(marker.getPosition().longitude));
                getAddress();

            }
        });
    }

    private void getAddress() {
        try {
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longtitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            Log.e("Address -", addresses.get(0).getAddressLine(0));
                //Log.e("FeatureName -", addresses.get(0).getFeatureName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            myAddress = addresses.get(0).getAddressLine(0);
            //  buttonAddAddress.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
