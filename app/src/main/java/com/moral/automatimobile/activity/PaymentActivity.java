package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.braintreepayments.cardform.view.CardForm;
import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.serializer.ObjectSerializer;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.card_form)
    CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(this);
//        if (cardForm.isCardScanningAvailable()) {
//            cardForm.scanCard(this);
//
//        } else {
//            Log.i("Card Form Scan", "Fail");
//        }

//        Intent intent = getIntent();
//        Color color = null;
//        Transmission transmission = null;
//        Engine engine = null;
//
//        try {
//             color = (Color) ObjectSerializer.deserialize(getIntent().getStringExtra("Color"));
//             transmission = (Transmission)ObjectSerializer.deserialize(getIntent().getStringExtra("Transmission"));
//             engine = (Engine) ObjectSerializer.deserialize(getIntent().getStringExtra("Engine"));
//
//            Log.i("Color", color.toString());
//            Log.i("Transmission", transmission.toString());
//            Log.i("Engine",engine.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
