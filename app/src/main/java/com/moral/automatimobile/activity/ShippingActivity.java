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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.State;
import com.moral.automatimobile.model.Transmission;
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

public class ShippingActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.nameShippingEditText)
    EditText nameShippingEditText;

    @BindView(R.id.addressShippingEditText)
    EditText addressShipEditText;

    @BindView(R.id.cityShippingEditText)
    EditText cityShipEditText;

    @BindView(R.id.stateShippingSpinner)
    Spinner stateShippingSpinner;

    String stateSelected = "";

    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        ButterKnife.bind(this);

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

        Call<List<State>> call = RetrofitClient.getInstance().getPersonService().getStates();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                loadStateList(response.body());
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load states", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadStateList(List<State> states) {
        final List<String> stateName = new ArrayList<>();
        for(State state: states) {
            stateName.add(state.getName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stateName);

        stateShippingSpinner.setAdapter(arrayAdapter);

        stateShippingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateSelected = stateName.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void onConfirm(View view) {
        String name = nameShippingEditText.getText().toString();
        String address = addressShipEditText.getText().toString();
        String city = cityShipEditText.getText().toString();

        Log.i("Name", name);
        Log.i("Address", address);
        Log.i("City", city);
        Log.i("State Selected", stateSelected);

        if(!validShipping(name, address, city)) {
            Toast.makeText(getApplicationContext(), "Invalid shipping input", Toast.LENGTH_LONG).show();
            return;
        }
        String[] temp = name.split("\\s+");
        String firstName = temp[0];
        String lastName = temp[1];
        String email = SaveSharedPreference.getEmail(getApplicationContext());
        Call<Person> call = RetrofitClient.getInstance().getPersonService().getPersonByEmail(email);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                person = response.body();
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {

            }
        });
        Shipping shipping = new Shipping(firstName,lastName, address, city, new State(stateSelected), person);
        Log.i("Shipping before save", shipping.toString());
        SaveSharedPreference.setShipping(getApplicationContext(),shipping);

        Intent intent = new Intent(getApplicationContext(), BuyActivity.class);
        startActivity(intent);
    }

    private boolean validShipping(String name, String address, String city) {

        boolean result = true;
        if(name.isEmpty() || !name.matches(".*\\s+.*")) {
            nameShippingEditText.setError("Enter a valid first and last name");
            result = false;
        } else {
            nameShippingEditText.setError(null);
        }

        if(address.isEmpty()) {
            addressShipEditText.setError("Enter a shipping address");
            result = false;
        } else {
            addressShipEditText.setError(null);
        }

        if(city.isEmpty()) {
            cityShipEditText.setError("Enter a city!");
            result = false;
        } else {
            cityShipEditText.setError(null);
        }
        return result;
    }
}
