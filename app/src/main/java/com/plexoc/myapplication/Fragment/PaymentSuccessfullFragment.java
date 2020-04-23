package com.plexoc.myapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.plexoc.myapplication.Activity.HomeActivity;
import com.plexoc.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentSuccessfullFragment extends BaseFragment {


    public PaymentSuccessfullFragment() {
        // Required empty public constructor
    }

    private MaterialButton btn_home;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_successfull, container, false);

        btn_home = view.findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
