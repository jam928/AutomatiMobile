package com.moral.automatimobile.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.activity.MainActivity;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Condition;
import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.State;
import com.moral.automatimobile.model.StatusCheck;
import com.moral.automatimobile.model.Transaction;
import com.moral.automatimobile.model.Transmission;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class BuyFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.paymentInfoTextView)
    TextView paymentInfoTextView;

    @BindView(R.id.shippingInfoTextView)
    TextView shippingInfoTextView;

    @BindView(R.id.carInfoTextView)
    TextView carInfoTextView;

    @BindView(R.id.confirmOrderButton)
    Button confirmOrderButton;

    @BindView(R.id.amountEnteredEditText)
    EditText amount;

    private Color color;
    private Transmission transmission;
    private Engine engine;
    private Model model;
    private CreditCard card;
    private Shipping shipping;
    private Car car;
    private boolean isNew;
    private Person person;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy,container,false);
        ButterKnife.bind(this, view);

        confirmOrderButton.setOnClickListener(this);
        // See if its a new car or used car
        String carCondition = SaveSharedPreference.getCarCondition(getContext());
        if(carCondition.equals("New Cars")) {
            // Load the car properties
            isNew = true;
            try {
                color = (Color) ObjectSerializer.deserialize(SaveSharedPreference.getColor(getContext()));
                transmission = (Transmission) ObjectSerializer.deserialize(SaveSharedPreference.getTransmission(getContext()));
                engine = (Engine) ObjectSerializer.deserialize(SaveSharedPreference.getEngine(getContext()));
                model = (Model) ObjectSerializer.deserialize(SaveSharedPreference.getModel(getContext()));

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
                car = (Car) ObjectSerializer.deserialize(SaveSharedPreference.getUsedCar(getContext()));
                car.setPerson(this.person);
                Log.i("Car", car.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // get credit card and shipping info
        try {
            card = (CreditCard) ObjectSerializer.deserialize(SaveSharedPreference.getCreditCard(getContext()));
            shipping = (Shipping) ObjectSerializer.deserialize(SaveSharedPreference.getShipping(getContext()));
            person = (Person) ObjectSerializer.deserialize(SaveSharedPreference.getPerson(getContext()));

        }catch (Exception e) {
            e.printStackTrace();
        }


        loadInfos();

        return view;
    }

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

    @Override
    public void onClick(View view) {
        Log.i("Clicked", "You've clicked the confirm order button!");
        Log.i("Amount", amount.getText().toString());
        Log.i("Shipping", shipping.toString());
        Log.i("CreditCard", card.toString());


        if(isNew) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            double price = model.getModelStockPrice() + color.getColorPrice() + transmission.getTransmissionPrice() + engine.getStockEnginePrice();
            String vin = RandomStringUtils.randomAlphabetic(17).toUpperCase();
            car = new Car(currentYear,0, "CLEAN", model, color, transmission, engine, new Condition("NEW"), price,vin,person);
        }

        double amo = 0;
        if(amount.getText().toString().equals("")) {
            amo = 0;
        } else {
            amo = Double.parseDouble(amount.getText().toString());
        }

        // validate amount
        if(!validateAmount(amo)) {
            Toast.makeText(getContext(), "Invalid Input Amount", Toast.LENGTH_LONG).show();
            return;
        }

        final Transaction transaction = new Transaction(amo, "Car Payment", person, card.getCreditCardNumber());

        // Set the spinning wheel thingy
//        final ProgressDialog progressDialog = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Adding Order...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        // add shipping address if its not in the db
        addShipping();

        // add creditcard if its not in the db
        addCard();

        // add or update car
        addOrUpdateCar();

        // update balance
        updateBalance(amo);

        // add transaction
        addTransaction(transaction);

        // Close the spiny thingy
//        progressDialog.dismiss();

        // Add 3 second delay to connect to the spring server and spit back a result

        Toast.makeText(getContext(), "Order added successfully!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void updateBalance(double amountEntered) {
        double oldBalance = this.person.getBalance();
        this.person.setBalance(oldBalance + this.car.getPrice() - amountEntered);
        Call<Object> call = RetrofitClient.getInstance().getPersonService().updateBalance(this.person);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("Balance", "Updated Successfully");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("Balance", "Update fail");
            }
        });
    }

    private void addTransaction(Transaction transaction) {
        Call<StatusCheck> call3 = RetrofitClient.getInstance().getPaymentService().addTransaction(transaction);
        call3.enqueue(new Callback<StatusCheck>() {
            @Override
            public void onResponse(Call<StatusCheck> call, Response<StatusCheck> response) {
                Log.i("Transaction", "Added Successfully");
            }

            @Override
            public void onFailure(Call<StatusCheck> call, Throwable t) {
                Log.i("Transaction", "fail to add");

            }
        });
    }

    private void addShipping() {
        if(shipping.getId() == 0) {
            Call<StatusCheck> call = RetrofitClient.getInstance().getPersonService().saveShipping(shipping);
            call.enqueue(new Callback<StatusCheck>() {
                @Override
                public void onResponse(Call<StatusCheck> call, Response<StatusCheck> response) {
                    Log.i("Shipping", "Added Successfully");

                }

                @Override
                public void onFailure(Call<StatusCheck> call, Throwable t) {
                    Log.i("Shipping", "failed to add");

                }
            });
        }
    }

    private void addCard() {
        if(card.getId() == 0) {
            Call<StatusCheck> call = RetrofitClient.getInstance().getPaymentService().saveCreditCard(card);
            call.enqueue(new Callback<StatusCheck>() {
                @Override
                public void onResponse(Call<StatusCheck> call, Response<StatusCheck> response) {
                    Log.i("Card", "Added Successfully");
                }

                @Override
                public void onFailure(Call<StatusCheck> call, Throwable t) {
                    Log.i("Card", "failed to add");
                }
            });
        }
    }

    private void addOrUpdateCar() {
        if(isNew) {
            Call<StatusCheck> call = RetrofitClient.getInstance().getCarService().addCar(car);
            call.enqueue(new Callback<StatusCheck>() {
                @Override
                public void onResponse(Call<StatusCheck> call, Response<StatusCheck> response) {
                    Log.i("Car", "Added Successfully");

                }

                @Override
                public void onFailure(Call<StatusCheck> call, Throwable t) {
                    Log.i("Car", "Failed To add");

                }
            });
        } else {
            Call<Object> call = RetrofitClient.getInstance().getCarService().updateCar(car);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.i("Car", "Added Successfully");

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.i("Car", "failed to add");

                }
            });
        }
    }

    private boolean validateAmount(double amo) {
        boolean result = true;

        if(amo > car.getPrice()) {
            amount.setError("Please enter an amount greater or less than car price");
            result = false;
        } else {
            amount.setError(null);
        }

        return result;
    }
}