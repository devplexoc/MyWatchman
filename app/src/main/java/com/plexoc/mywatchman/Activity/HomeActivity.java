package com.plexoc.mywatchman.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.plexoc.mywatchman.Fragment.DashboardFragment;
import com.plexoc.mywatchman.Fragment.PlansFragment;
import com.plexoc.mywatchman.Fragment.RaisedSOSFragment;
import com.plexoc.mywatchman.Fragment.RoamingStaffIdentityFragment;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;

import java.util.Objects;

public class HomeActivity extends BaseActivity {

    private int id, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        try {
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.getStringExtra("id") != null) {
                    id = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("id")));
                    status = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("status")));
                    //  Log.e("status", intent.getStringExtra("status"));
                    if (status == 1) {
                        Constants.isFromNotification = true;
                        replaceFragment(new RoamingStaffIdentityFragment(id), RoamingStaffIdentityFragment.class.getName());
                    } else if (status == 0) {
                        Constants.isFromNotification = true;
                        replaceFragment(new RaisedSOSFragment(id), RaisedSOSFragment.class.getName());
                    } else
                        addFragment(new DashboardFragment());
                }else {
                    addFragment(new DashboardFragment());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  public void replaceFragment(Fragment fragment, String fragmentTag) {

        if (fragmentTag == null) {
            getSupportFragmentManager().popBackStack(fragment.getClass().getName(), 1);
        } else {
            getSupportFragmentManager().popBackStack(fragmentTag, 1);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(fragmentTag == null ? fragment.getClass().getName() : fragmentTag);
        transaction.commitAllowingStateLoss();

    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }*/

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof DashboardFragment) {
            finish();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof PlansFragment) {
            finish();
        } else if (Constants.isFromNotification) {
            replaceFragment(new DashboardFragment(), DashboardFragment.class.getName());
        } else {
            super.onBackPressed();
        }
    }
}
