package com.plexoc.myapplication.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.myapplication.Adapter.TransctionHistoryAdpter;
import com.plexoc.myapplication.Model.TransactionHistory;
import com.plexoc.myapplication.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TranscationHistoryFragment extends BaseFragment {


    public TranscationHistoryFragment() {
        // Required empty public constructor
    }

    List<TransactionHistory> transactionlist;
    RecyclerView recyclerViewTransctionHistory;
    TransctionHistoryAdpter transctionHistoryAdpter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transcation_history, container, false);

        RecyclerView recyclerViewTransctionHistory = view.findViewById(R.id.recyclerview_transction_history);

        TransctionHistoryAdpter transctionHistoryAdpter = new TransctionHistoryAdpter(transactionlist,getContext());
        recyclerViewTransctionHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTransctionHistory.setAdapter(transctionHistoryAdpter);

        return view;
    }

}
