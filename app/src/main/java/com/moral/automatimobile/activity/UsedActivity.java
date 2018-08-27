package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.adapter.ListAdapter;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsedActivity extends AppCompatActivity {

    @BindView(R.id.usedModelsListView)
    ListView usedModelsListView;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    ListAdapter listAdapter;

    Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used);

        ButterKnife.bind(this);

        // check if the user is logged in
        boolean isLoggedIn = SaveSharedPreference.getLoggedStatus(getApplicationContext());

        if(isLoggedIn) {
            navigation.getMenu().removeItem(R.id.navigation_login);
            navigation.getMenu().removeItem(R.id.navigation_register);
        } else {
            navigation.getMenu().removeItem(R.id.navigation_profile);
            navigation.getMenu().removeItem(R.id.navigation_logout);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        try {
            model = (Model)ObjectSerializer.deserialize(getIntent().getStringExtra("used_model"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Call<List<Car>> call = RetrofitClient.getInstance().getCarService().getCarsByModel(model.getName());
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if(response.isSuccessful()) {
                    Log.i("Response", "Success");

                    List<Car> cars = response.body();

                    loadCarsToView(cars);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Log.i("Response", "Fail");
            }
        });
    }

    private void loadCarsToView(final List<Car> cars) {
        final List<String> imgSrcs = new ArrayList<>();
        final List<String> topInfo = new ArrayList<>();
        List<String> bottomInfo = new ArrayList<>();

        for(Car car: cars) {
            imgSrcs.add(car.getModel().getImgSrc());
            bottomInfo.add("$" + car.getPrice() + " Mileage: " + car.getMileage());
            topInfo.add(car.getYear() + " Automati " + car.getModel().getName());
        }

        this.listAdapter = new ListAdapter(getApplicationContext(), topInfo, bottomInfo, imgSrcs);
        usedModelsListView.setAdapter(this.listAdapter);

        usedModelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i("TopInfo", topInfo.get(i));
                Intent intent = new Intent(getApplicationContext(), CarDetailActivity.class);
                SaveSharedPreference.setUsedCar(getApplicationContext(), cars.get(i));
                startActivity(intent);

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                case R.id.navigation_login:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    return true;
                case R.id.navigation_register:
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    return true;
                case R.id.navigation_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
                case R.id.navigation_logout:
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "none");
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    return true;
            }
            return false;
        }
    };

}
