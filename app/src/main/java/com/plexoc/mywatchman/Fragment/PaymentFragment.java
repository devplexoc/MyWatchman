package com.plexoc.mywatchman.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plexoc.mywatchman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends BaseFragment {

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

     /*   cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup*/

     view.findViewById(R.id.btn_make_payment).setOnClickListener(v -> replaceFragment(new PaymentSuccessfullFragment(),null));

        return view;
    }
}
