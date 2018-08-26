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
import com.moral.automatimobile.session.SaveSharedPreference;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        id = intent.getIntExtra("car_id", 0);

        Log.i("ID", Integer.toString(id));

        Call<Car> call = RetrofitClient.getInstance().getCarService().getCarById(id);

        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if(response.isSuccessful()) {
                    Car car = response.body();
                    Log.i("Car", car.toString());

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
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                    Log.i("Car", "Car failed to load");
            }
        });

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
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    goToPage("Home");
                    return true;
                case R.id.navigation_login:
                    goToPage("Login");
                    return true;
                case R.id.navigation_register:
                    goToPage("Register");
                    return true;
                case R.id.navigation_profile:
                    goToPage("Profile");
                    return true;
                case R.id.navigation_logout:
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                    finish();
                    startActivity(getIntent());
                    return true;
            }
            return false;
        }
    };

    private void goToPage(String page) {
        Intent intent;
        switch (page) {
            case "Login":
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case "Register":
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case "Home":
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }
    }

    public void onBuy(View view) {
        Log.i("Car_id", Integer.toString(id));
    }
}
