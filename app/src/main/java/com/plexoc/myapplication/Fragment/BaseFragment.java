package com.plexoc.myapplication.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.plexoc.myapplication.Activity.HomeActivity;
import com.plexoc.myapplication.Activity.LoginSignupActivity;
import com.plexoc.myapplication.Model.Address;
import com.plexoc.myapplication.Model.User;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Service.ApiClient;
import com.plexoc.myapplication.Service.AppService;
import com.plexoc.myapplication.Utils.Prefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BaseFragment extends Fragment {

    private ApiClient apiClient;
    User user;

    public BaseFragment() {
        // Required empty public constructor
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User();
        apiClient = AppService.createService(ApiClient.class);
        if (!Prefs.getString(Prefs.USER).isEmpty())
            user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {
        if (getActivity() instanceof LoginSignupActivity) {

            ((LoginSignupActivity) getActivity()).replaceFragment(fragment, fragmentTag);
        }
        if (getActivity() instanceof HomeActivity) {

            ((HomeActivity) getActivity()).replaceFragment(fragment, fragmentTag);
        }

    }

    public void addFragment(Fragment fragment) {
        if (getActivity() instanceof LoginSignupActivity) {

            ((LoginSignupActivity) getActivity()).addFragment(fragment);
        } if (getActivity() instanceof HomeActivity) {

            ((HomeActivity) getActivity()).addFragment(fragment);
        }
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return mNetworkInfo != null;
        } catch (NullPointerException e) {
            return false;
        }
    }


    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidMobile(final String phone) {

        String PhonePattern = "(0/91)?[6-9][0-9]{9}";
        Pattern pattern = Pattern.compile(PhonePattern);
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
        //Pattern pattern = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        //return pattern.matcher(phone).matches();
        //  return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void showMessage(String message) {

        if (getActivity() != null) {

            Snackbar mSnackBar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
            View view = mSnackBar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.BOTTOM;
            view.setLayoutParams(params);
            view.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            TextView mainTextView = (TextView) (view).findViewById(R.id.snackbar_text);
            mainTextView.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            mainTextView.setTextSize(12.0f);
            try {
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.work_sans_bold);
                mainTextView.setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainTextView.setTextColor(Color.WHITE);
            mSnackBar.show();
        }
    }

    public void closeKeybord() {

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
