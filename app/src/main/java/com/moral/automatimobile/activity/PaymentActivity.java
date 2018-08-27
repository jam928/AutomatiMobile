package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

//    @BindView(R.id.card_form)
//    CardForm cardForm;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.cardNumberEditText)
    EditText cardNumberEditText;

    @BindView(R.id.cvvEditText)
    EditText cvvEditText;

    @BindView(R.id.cardDateEditText)
    EditText cardDateEditText;

    private Person person;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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

        if(!SaveSharedPreference.getEmail(getApplicationContext()).equals("none")) {
            Call<Person> call = RetrofitClient.getInstance().getPersonService().getPersonByEmail(SaveSharedPreference.getEmail(getApplicationContext()));

            call.enqueue(new Callback<Person>() {
                @Override
                public void onResponse(Call<Person> call, Response<Person> response) {
                    if (response.isSuccessful()) {
                        person = response.body();
                        Log.i("Person from payment", person.toString());
                    }
                }

                @Override
                public void onFailure(Call<Person> call, Throwable t) {

                }
            });
        } else {
            Log.i("Login", "Login to continue");
        }

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

    public void onShip(View view) {
        final String cardNumber = cardNumberEditText.getText().toString().replaceAll(" ", "");
        final String cardDate = cardDateEditText.getText().toString();
        int csc = 0;
        if(cvvEditText.getText().toString().equals("")) {
            csc = 0;
        } else {
            csc = Integer.parseInt(cvvEditText.getText().toString());
        }

        if(!validateCard(cardNumber, cardDate, csc)) {
            Toast.makeText(getApplicationContext(), "Invalid card", Toast.LENGTH_LONG).show();
            return;
        }

        Log.i("Congrats", "You entered a valid card");

        Intent intent = new Intent(getApplicationContext(), ShippingActivity.class);
        CreditCard card = new CreditCard(cardNumber, cardDate, csc, person);
        try {
            intent.putExtra("Credit Card", ObjectSerializer.serialize(card));
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean validateCard(String cardNumber, String cardDate, int csc) {
        boolean result = true;

        // Check if the cardNumber is valid
        if(cardNumber.isEmpty() || cardNumber.length() != 16) {
            cardNumberEditText.setError("Enter a valid 16 digit credit card");
            result = false;
        } else {
            cardNumberEditText.setError(null);
        }

        // check if the card date is valid
        if(cardDate.isEmpty() || !validDate(cardDate)) {
            cardDateEditText.setError("Please enter a valid date YYYY-MM");
            result = false;
        } else {
            cardDateEditText.setError(null);
        }

        // Check if the csc is valid
        if(!isTripleDigit(csc)) {
            cvvEditText.setError("Enter a valid CVV e.g XXX");
            result = false;
        } else {
            cvvEditText.setError(null);
        }
        return result;
    }

    private boolean isTripleDigit(int csc) {
        return 100 <= csc && csc <= 999;
    }

    private boolean validDate(String cardDate) {

        // Date format of YYYY-MM
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM");
        simpleDateFormat.setLenient(false);

        try {
            Date date = simpleDateFormat.parse(cardDate);
            return true;
        } catch (ParseException e){
            e.printStackTrace();
            return false;
        }

    }
}
