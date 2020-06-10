package com.plexoc.mywatchman.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.mywatchman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RaisedSOSFragment extends BaseFragment {


    public RaisedSOSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_raised_so, container, false);

        RecyclerView recyclerViewRaisedSOS = view.findViewById(R.id.recyclerview_raised_sos);
        /*RaisedSOSAdpter raisedSOSAdpter = new RaisedSOSAdpter();
        recyclerViewRaisedSOS.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRaisedSOS.setAdapter(raisedSOSAdpter);*/

        return view;
    }

}
