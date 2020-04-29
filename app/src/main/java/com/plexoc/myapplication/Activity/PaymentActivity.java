package com.plexoc.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.braintreepayments.cardform.view.CardForm;
import com.plexoc.myapplication.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Make Payment");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        CardForm cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Make Payment")
                .setup(this);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

    }
}
