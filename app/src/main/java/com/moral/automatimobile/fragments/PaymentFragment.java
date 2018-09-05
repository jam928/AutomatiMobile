package com.moral.automatimobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.cardNumberEditText)
    EditText cardNumberEditText;

    @BindView(R.id.cvvEditText)
    EditText cvvEditText;

    @BindView(R.id.cardDateEditText)
    EditText cardDateEditText;

    @BindView(R.id.paymentButton)
    Button paymentButton;

    private Person person;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment,container,false);
        ButterKnife.bind(this, view);

        paymentButton.setOnClickListener(this);

        if(!SaveSharedPreference.getEmail(getContext()).equals("none")) {
            Call<Person> call = RetrofitClient.getInstance().getPersonService().getPersonByEmail(SaveSharedPreference.getEmail(getContext()));

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


        return view;
    }


    @Override
    public void onClick(View view) {

        final String cardNumber = cardNumberEditText.getText().toString().replaceAll(" ", "");
        final String cardDate = cardDateEditText.getText().toString();
        int csc = 0;
        if(cvvEditText.getText().toString().equals("")) {
            csc = 0;
        } else {
            csc = Integer.parseInt(cvvEditText.getText().toString());
        }

        if(!validateCard(cardNumber, cardDate, csc)) {
            Toast.makeText(getContext(), "Invalid card", Toast.LENGTH_LONG).show();
            return;
        }
//        Log.i("CreditCardNumber", cardNumber);
//        Log.i("CardDate", cardDate);
//        Log.i("CSC", Integer.toString(csc));
//        Log.i("Congrats", "You entered a valid card");

        CreditCard card = new CreditCard(cardNumber, cardDate, csc, person);
        Log.i("Credit Card before save", card.toString());
        SaveSharedPreference.setCard(getContext(), card);

        Fragment fragment = new ShippingFragment();
        loadFragment(fragment);

    }

    private void loadFragment(Fragment fragment) {
        if(fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
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