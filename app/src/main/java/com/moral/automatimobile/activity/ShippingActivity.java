package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.io.IOException;

public class ShippingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

//        Color color = null;
//        Transmission transmission = null;
//        Engine engine = null;
//
//        try {
//             color = (Color) ObjectSerializer.deserialize(SaveSharedPreference.getColor(getApplicationContext()));
//             transmission = (Transmission)ObjectSerializer.deserialize(SaveSharedPreference.getTransmission(getApplicationContext()));
//             engine = (Engine) ObjectSerializer.deserialize(SaveSharedPreference.getEngine(getApplicationContext()));
//
//            Log.i("Color", color.toString());
//            Log.i("Transmission", transmission.toString());
//            Log.i("Engine",engine.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
