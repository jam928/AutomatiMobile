package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailActivity extends AppCompatActivity {

    @BindView(R.id.carDetailImageView)
    ImageView carDetailImageView;

    @BindView(R.id.detailsTextView)
    TextView detailsTextView;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    int id;

    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        ButterKnife.bind(this);

        try {
            car = (Car) ObjectSerializer.deserialize(SaveSharedPreference.getUsedCar(getApplicationContext()));
            // Load image to imageview
            Picasso.get().load(car.getModel().getImgSrc()).into(carDetailImageView);

            // Load car details to textview
            String text = car.getCondition().getType() + " " + car.getYear() + " Automati " + car.getModel().getName() + "\n" +
                    "Price: " + car.getPrice() + " USD" + "\n" +
                    "Mileage: " + car.getMileage() + "\n" +
                    "Transmission: " + car.getTransmission().getName() + "\n" +
                    "Title: " + car.getTitle() + "\n" +
                    "Color: " + car.getColor().getName() + "\n" +
                    "Vin: " + car.getVin() + "\n" +
                    "Engine: " + car.getEngine().getLitres() + "L " + car.getEngine().getCylinders() + " cylinders";
            detailsTextView.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // check if the user is logged in
        boolean isLoggedIn = SaveSharedPreference.getLoggedStatus(getApplicationContext());

        if(isLoggedIn) {
            navigation.getMenu().removeItem(R.id.navigation_login);
            navigation.getMenu().removeItem(R.id.navigation_register);
        } else {
            navigation.getMenu().removeItem(R.id.navigation_profile);
            navigation.getMenu().removeItem(R.id.navigation_logout);
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    finish();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_login:
                    finish();
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_register:
                    finish();
                    intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    finish();
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_logout:
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "none");
                    finish();
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    public void onBuy(View view) {
        Log.i("Car_id", Integer.toString(id));

         Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
         startActivity(intent);
         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
