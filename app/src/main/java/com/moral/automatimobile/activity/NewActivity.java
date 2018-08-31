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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewActivity extends AppCompatActivity {

    @BindView(R.id.newCarImageView)
    ImageView newCarImageView;

    @BindView(R.id.colorSpinner)
    Spinner colorSpinner;

    @BindView(R.id.transmissionSpinner)
    Spinner transmissionSpinner;

    @BindView(R.id.engineSpinner)
    Spinner engineSpinner;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private List<Color> colors;
    private List<Transmission> transmissions;
    private List<Engine> engines;
    private int colorSelected;
    private int transmissionSelected;
    private int engineSelected;


    Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

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
            model = (Model) ObjectSerializer.deserialize(SaveSharedPreference.getModel(getApplicationContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Model", model.toString());

        // Get car properties
        loadCarProperties();

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colorSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                colorSelected = 0;
            }
        });

        transmissionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transmissionSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                transmissionSelected = 0;
            }
        });

        engineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                engineSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                engineSelected = 0;
            }
        });

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



    private void loadCarProperties() {

        // Load imgSrc to the imageview
        Picasso.get().load(model.getImgSrc()).into(newCarImageView);

        Call<List<Color>> call = RetrofitClient.getInstance().getCarService().getColors();

        call.enqueue(new Callback<List<Color>>() {
            @Override
            public void onResponse(Call<List<Color>> call, Response<List<Color>> response) {
                if(response.isSuccessful()) {
                    colors = response.body();

                    Log.i("Color", colors.toString());
                    ArrayList<String> colorInfo = new ArrayList<>();
                    // add colors to the spinner
                    for(Color color: colors) {
                        colorInfo.add(color.getName() + " Price: $" + color.getColorPrice());
                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, colorInfo);

                    colorSpinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Color>> call, Throwable t) {
                    Log.i("Color", "Failed to load colors");
            }
        });

        Call<List<Transmission>> call2 = RetrofitClient.getInstance().getCarService().getTransmissions();
        call2.enqueue(new Callback<List<Transmission>>() {
            @Override
            public void onResponse(Call<List<Transmission>> call, Response<List<Transmission>> response) {
                if(response.isSuccessful()){
                    transmissions = response.body();

                    Log.i("Transmission", transmissions.toString());

                    ArrayList<String> transmissionInfo = new ArrayList<>();

                    // add transmission to the spinner
                    for(Transmission transmission: transmissions) {
                        transmissionInfo.add(transmission.getName() + " Price: $" + transmission.getTransmissionPrice());
                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, transmissionInfo);

                    transmissionSpinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Transmission>> call, Throwable t) {
                Log.i("Transmission", "Failed to load transmissions");
            }
        });

        Call<List<Engine>> call3 = RetrofitClient.getInstance().getCarService().getEngines();
        call3.enqueue(new Callback<List<Engine>>() {
            @Override
            public void onResponse(Call<List<Engine>> call, Response<List<Engine>> response) {
                if(response.isSuccessful()){
                    engines = response.body();

                    Log.i("Engine", engines.toString());

                    ArrayList<String> engineInfo = new ArrayList<>();

                    // add transmission to the spinner
                    for(Engine engine: engines) {
                        engineInfo.add(engine.getLitres() + "L " + engine.getCylinders() + "cylinder engine Price: $" + engine.getStockEnginePrice());
                    }
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, engineInfo);

                    engineSpinner.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Engine>> call, Throwable t) {
                Log.i("Engine", "Failed to load engines");
            }
        });
    }

    public void onPay(View view) {
        Log.i("Clicked", "On Pay");
        Log.i("Color Selected", colors.get(colorSelected).toString());
        Log.i("Transmission Selected", transmissions.get(transmissionSelected).toString());
        Log.i("Engine Selected", engines.get(engineSelected).toString());

        SaveSharedPreference.setNewCarProperties(getApplicationContext(), colors.get(colorSelected), transmissions.get(transmissionSelected), engines.get(engineSelected));
        SaveSharedPreference.setCarCondition(getApplicationContext(), "New Car");

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
