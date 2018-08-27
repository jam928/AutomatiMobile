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
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.adapter.ListAdapter;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.network.RetrofitClient;
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

public class ModelActivity extends AppCompatActivity {

    @BindView(R.id.modelsListView)
    ListView modelsListView;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.loginCondTextView)
    TextView loginCondTextView;

    ListAdapter listAdapter;

    boolean isNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        ButterKnife.bind(this);

        // check if the user is logged in
        boolean isLoggedIn = SaveSharedPreference.getLoggedStatus(getApplicationContext());

        if(isLoggedIn) {
            navigation.getMenu().removeItem(R.id.navigation_login);
            navigation.getMenu().removeItem(R.id.navigation_register);
            loginCondTextView.setVisibility(View.INVISIBLE);
        } else {
            navigation.getMenu().removeItem(R.id.navigation_profile);
            navigation.getMenu().removeItem(R.id.navigation_logout);
            loginCondTextView.setVisibility(View.VISIBLE);
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Check if the user click new or used
        String cond = getIntent().getStringExtra("Car");
        if(cond != null) {
            if(cond.equals("newCar")) {
                isNew = true;
                Log.i("You Clicked", "New");
            }
            else {
                Log.i("You Clicked", "Used");
                isNew = false;
            }
        }
        // Get models from the rest end point
        Call<List<Model>> call = RetrofitClient.getInstance().getCarService().getModels();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(response.isSuccessful()) {
                    Log.i("Response", "Success");
                    List<Model> models = response.body();
//                    for(Model model: models) {
//                        Log.i("Model", model.toString());
//                    }
                    loadModelsToView(models);
                } else {
                    Log.i("Response", "Failed");
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {

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


    private void loadModelsToView(final List<Model> models) {
        final List<String> imgSrcs = new ArrayList<>();
        final List<String> topInfo = new ArrayList<>();
        List<String> bottomInfo = new ArrayList<>();

        for(Model model: models) {
            imgSrcs.add(model.getImgSrc());
            topInfo.add("Automati " + model.getName());
            bottomInfo.add(model.getDescription());
        }

        this.listAdapter = new ListAdapter(getApplicationContext(), topInfo, bottomInfo, imgSrcs);
        modelsListView.setAdapter(this.listAdapter);

        modelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(!isNew) {
                    Intent intent = new Intent(getApplicationContext(), UsedActivity.class);
                    try {
                        intent.putExtra("used_model", ObjectSerializer.serialize(models.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                    try {
                        intent.putExtra("Model", ObjectSerializer.serialize(models.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                }
            }
        });
    }
}
