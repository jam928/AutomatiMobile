package com.moral.automatimobile.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.StatusCheck;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class BuyActivity extends AppCompatActivity {

    @BindView(R.id.paymentInfoTextView)
    TextView paymentInfoTextView;

    @BindView(R.id.shippingInfoTextView)
    TextView shippingInfoTextView;

    @BindView(R.id.carInfoTextView)
    TextView carInfoTextView;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;


    private Color color;
    private Transmission transmission;
    private Engine engine;
    private Model model;
    private CreditCard card;
    private Shipping shipping;
    private Car car;
    private boolean isNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        ButterKnife.bind(this);
        // See if its a new car or used car
        String carCondition = SaveSharedPreference.getCarCondition(getApplicationContext());
        if(carCondition.equals("New Car")) {
            // Load the car properties
            isNew = true;
            try {
                 color = (Color)ObjectSerializer.deserialize(SaveSharedPreference.getColor(getApplicationContext()));
                 transmission = (Transmission) ObjectSerializer.deserialize(SaveSharedPreference.getTransmission(getApplicationContext()));
                 engine = (Engine) ObjectSerializer.deserialize(SaveSharedPreference.getEngine(getApplicationContext()));
                 model = (Model) ObjectSerializer.deserialize(SaveSharedPreference.getModel(getApplicationContext()));

                Log.i("Color", color.toString());
                Log.i("Transmission", transmission.toString());
                Log.i("Engine", engine.toString());
                Log.i("Model", model.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                isNew = false;
                car = (Car) ObjectSerializer.deserialize(SaveSharedPreference.getUsedCar(getApplicationContext()));
                Log.i("Car", car.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // get credit card and shipping info
        try {
            card = (CreditCard) ObjectSerializer.deserialize(SaveSharedPreference.getCreditCard(getApplicationContext()));
            shipping = (Shipping) ObjectSerializer.deserialize(SaveSharedPreference.getShipping(getApplicationContext()));
        }catch (Exception e) {
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

        loadInfos();
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

    private void loadInfos() {
        String cardInfo = "Number: " + card.getCreditCardNumber() + "\n" +
                            "Exp Date: " + card.getExpirationDate() + "\n" +
                            "CSC: " + card.getCsc();
        paymentInfoTextView.setText(cardInfo);

        String shippingInfo = "Name: " + shipping.getFirstName() + " " + shipping.getLastName() + "\n" +
                "Address: " + shipping.getStreet() + " " + shipping.getCity() + " " + shipping.getState().getName() + "\n";
        shippingInfoTextView.setText(shippingInfo);

        // load carInfo
        String carInfo = "";
        Calendar now = Calendar.getInstance();
        if(isNew) {
            double price = model.getModelStockPrice() + color.getColorPrice() + transmission.getTransmissionPrice() + engine.getStockEnginePrice();
            carInfo = "NEW" + " " + now.get(Calendar.YEAR) + " Automati " + model.getName() + "\n" +
                    "Price: " + price + " USD" + "\n" +
                    "Mileage: " + "0" + "\n" +
                    "Transmission: " + transmission.getName() + "\n" +
                    "Title: " + "CLEAN" + "\n" +
                    "Color: " + color.getName() + "\n" +
                    "Engine: " + engine.getLitres() + "L " + engine.getCylinders() + " cylinders";
        } else{
            carInfo = car.getCondition().getType() + " " + car.getYear() + " Automati " + car.getModel().getName() + "\n" +
                    "Price: " + car.getPrice() + " USD" + "\n" +
                    "Mileage: " + car.getMileage() + "\n" +
                    "Transmission: " + car.getTransmission().getName() + "\n" +
                    "Title: " + car.getTitle() + "\n" +
                    "Color: " + car.getColor().getName() + "\n" +
                    "Vin: " + car.getVin() + "\n" +
                    "Engine: " + car.getEngine().getLitres() + "L " + car.getEngine().getCylinders() + " cylinders";
        }
        carInfoTextView.setText(carInfo);
    }

    public void confirmOrder(View view) {


        Log.i("Credit Card", card.toString());
        Log.i("Shipping", shipping.toString());
        Log.i("Model", model.toString());

        // Save credit card
//        if(card.getId() == 0) {
//            Call<StatusCheck> call = RetrofitClient.getInstance().getPaymentService().saveCreditCard(card);
//        }

        // Save shipping address
//        if(shipping.getId() == 0) {
//            Call<StatusCheck> call = RetrofitClient.getInstance().getPersonService().saveShipping(shipping);
//        }




    }
}
