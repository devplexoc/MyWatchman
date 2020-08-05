package com.plexoc.mywatchman.Fragment;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.plexoc.mywatchman.Model.RaisedSOSUser;
import com.plexoc.mywatchman.Model.Response;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.CirclurProgressDrawable;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.DirectionsJSONParser;
import com.plexoc.mywatchman.Utils.LoadingDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoamingStaffIdentityFragment extends BaseFragment implements OnMapReadyCallback {

    private int SOSID;
    private RaisedSOSUser raisedSOSUser;
    private Double destinationlat, destinationlong, originlat, originLong;
    private LatLng origin, dest;
    private boolean isHandlerStopped = false;
    private String duration, distance;

    private Handler handler;
    private Runnable runnable;


    public RoamingStaffIdentityFragment(int SOSID) {
        this.SOSID = SOSID;
    }

    private View mapFragment;
    private GoogleMap mMap;
    private static final String MAP_VIEW_BUNDLE_KEY = "AIzaSyC6BHeJdoVNzD2PcBKGqxlFcYcddQFkWD8";

    private AppCompatTextView textview_roaming_staff_name, textview_roaming_staff_age, textview_raised_sos_date, textview_raised_sos_time, textview_help_reached;
    private RatingBar ratingbar_roaming_staff_rating;
    private String ResponderName;
    private AppCompatImageView imageView_roamingStaff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_roaming_staff_identity, container, false);

        raisedSOSUser = new RaisedSOSUser();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Roaming Staff");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> {
            if (Constants.isFromNotification)
                replaceFragment(new DashboardFragment(), DashboardFragment.class.getName());
            else
                getActivity().onBackPressed();
        });

        textview_roaming_staff_name = view.findViewById(R.id.textview_roaming_staff_name);
        textview_roaming_staff_age = view.findViewById(R.id.textview_roaming_staff_age);
        textview_raised_sos_date = view.findViewById(R.id.textview_raised_sos_date);
        textview_raised_sos_time = view.findViewById(R.id.textview_raised_sos_time);
        textview_help_reached = view.findViewById(R.id.textview_help_reached);
        ratingbar_roaming_staff_rating = view.findViewById(R.id.ratingbar_roaming_staff_rating);

        imageView_roamingStaff = view.findViewById(R.id.imageview_roaming_staff_image);

        // mapFragment = view.findViewById(R.id.map);


        ConstraintLayout constraintLayoutRating = view.findViewById(R.id.constraintlayout_roaming_staff_rating);
        MaterialButton btnVerigyStaff = view.findViewById(R.id.btn_validate_roaming_staff);

        btnVerigyStaff.setOnClickListener(v -> {
            btnVerigyStaff.setVisibility(View.GONE);
            constraintLayoutRating.setVisibility(View.VISIBLE);
        });

        MaterialButton btnSubmitRating = view.findViewById(R.id.btn_submit_rating);

        btnSubmitRating.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thank you for the ratings.", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        });

       /* if (raisedSOSUser.ResponserName == null) {
            textview_roaming_staff_name.setText("Responder not assigned yet.");
            ratingbar_roaming_staff_rating.setVisibility(View.GONE);
            textview_roaming_staff_age.setVisibility(View.GONE);
        }
        if (raisedSOSUser.CompletedSOSInMin == null) {
            textview_help_reached.setText(" - ");
        }
        if (raisedSOSUser.StartDate == null) {
            textview_raised_sos_date.setText(" - ");
        }
        if (raisedSOSUser.StartTime == null) {
            textview_raised_sos_time.setText(" - ");
        }*/

       /* handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {
                if(!isHandlerStopped){
                    getSOSById();
                    Toast.makeText(getContext(), "Api Call", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runnable,10000);
                }
            }
        };
        handler.post(runnable);*/

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

                        if (response.body().Item.ResponserName == null) {
                            textview_roaming_staff_name.setText("Roaming staff not assigned yet.");
                            ratingbar_roaming_staff_rating.setVisibility(View.GONE);
                            textview_roaming_staff_age.setVisibility(View.GONE);
                        } else {
                            ResponderName = response.body().Item.ResponserName;
                            textview_roaming_staff_name.setText(response.body().Item.ResponserName);

                            if (response.body().Item.RoamingStaffPosition != null) {
                                textview_roaming_staff_age.setVisibility(View.VISIBLE);
                                textview_roaming_staff_age.setText("Position : " + response.body().Item.RoamingStaffPosition);
                            }else {
                                textview_roaming_staff_age.setVisibility(View.GONE);
                            }
                        }


                        try {
                            if (response.body().Item.RoamingStaffImageUrl != null) {
                                Glide.with(getContext())
                                        .load(response.body().Item.RoamingStaffImageUrl)
                                        .thumbnail(0.25f)
                                        .circleCrop()
                                        .placeholder(CirclurProgressDrawable.ShowProgressDrwable(getContext()))
                                        .error(R.drawable.ic_user_colorful)
                                        .into(imageView_roamingStaff);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (response.body().Item.CompletedSOSInMin == null) {
                            textview_help_reached.setText(" - ");
                        } else {
                            textview_help_reached.setText(response.body().Item.CompletedSOSInMin);
                        }

                        if (response.body().Item.StartDate == null) {
                            textview_raised_sos_date.setText(" - ");
                        } else {
                            textview_raised_sos_date.setText(response.body().Item.StartDate);
                        }

                        if (response.body().Item.StartTime == null) {
                            textview_raised_sos_time.setText(" - ");
                        } else {
                            textview_raised_sos_time.setText(response.body().Item.StartTime);
                        }

                        if (response.body().Item.Latitude != null) {
                            destinationlat = Double.parseDouble(response.body().Item.Latitude);
                        }
                        if (response.body().Item.Longitude != null) {
                            destinationlong = Double.parseDouble(response.body().Item.Longitude);
                        }
                        if (response.body().Item.ResponderLat != null) {
                            originlat = Double.parseDouble(response.body().Item.ResponderLat);
                        }
                        if (response.body().Item.ResponderLong != null) {
                            originLong = Double.parseDouble(response.body().Item.ResponderLong);
                        }

                        if (destinationlat != null || destinationlong != null) {
                            mMap.clear();
                            dest = new LatLng(destinationlat, destinationlong);
                            mMap.addMarker(new MarkerOptions().position(dest).title("You are here"));


                            if (originlat != null || originLong != null) {
                                origin = new LatLng(originlat, originLong);
                                // mMap.addMarker(new MarkerOptions().position(origin).title("Responder").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.addMarker(new MarkerOptions().position(origin).title(ResponderName).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_black_red)));

                                // Getting URL to the Google Directions API
                                String url = getDirectionsUrl(origin, dest);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions API
                                downloadTask.execute(url);

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(dest);
                                LatLngBounds bounds = builder.build();

                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 2000, null);
                            } else {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
                            }
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
        mMap.setMinZoomPreference(8);
        mMap.setMaxZoomPreference(22);
        mMap.setIndoorEnabled(true);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isHandlerStopped) {
                    getSOSById();
                    //Toast.makeText(getContext(), "Api Call", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(runnable, 10000);
                }
            }
        };
        handler.post(runnable);

        getSOSById();

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        /*LatLng latLng = new LatLng(23.0204978, 72.4396562);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).draggable(true);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));*/

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).draggable(true);
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

               *//* latitude = String.valueOf(latLng.latitude);
                longtitude = String.valueOf(latLng.longitude);*//*
            }
        });*/
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }
            if (duration != null)
                textview_help_reached.setText(duration);
            else
                textview_help_reached.setText(" - ");
            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&" + "key=AIzaSyDby5O_0c4dCBX50h_8RDYaXuA619oA9lY";


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

   /* @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable);
    }*/
}
