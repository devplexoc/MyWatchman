package com.plexoc.myapplication.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.plexoc.myapplication.Adapter.AddressAdpter;
import com.plexoc.myapplication.Adapter.ContactAdpter;
import com.plexoc.myapplication.Adapter.TabAdpter;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.DrawerUtil;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmergencyContactFragment extends BaseFragment {


    public EmergencyContactFragment() {
        // Required empty public constructor
    }

    private static TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency_contact, container, false);


        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("My Community");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        DrawerUtil.getDrawer(getActivity(), toolbar, user);


        ViewPager viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);

        TabAdpter tabAdpter = new TabAdpter(getChildFragmentManager());
        tabAdpter.addFragment(new MyCommunityFragment(), "My Community");
        //tabAdpter.addFragment(new PendingCommunityListFragment(), "Pending List");
        tabAdpter.addFragment(new CommunityRequestFragment(), "Community Request");

        viewPager.setAdapter(tabAdpter);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

   /* private void OpenDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_add_emergency_contact, null);
        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();
    }
*/

    public static void setBadgeCount(int Count) {
        Objects.requireNonNull(tabLayout.getTabAt(1)).showBadge().setNumber(Count);
    }

    public static void removeBadgeCount() {
        Objects.requireNonNull(tabLayout.getTabAt(1)).removeBadge();
    }
}
