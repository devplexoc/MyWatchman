package com.plexoc.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.plexoc.myapplication.Fragment.DashboardFragment;
import com.plexoc.myapplication.Fragment.LoginFragment;
import com.plexoc.myapplication.Fragment.PlansFragment;
import com.plexoc.myapplication.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addFragment(new DashboardFragment());
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
        } else {
            super.onBackPressed();
        }
    }
}
