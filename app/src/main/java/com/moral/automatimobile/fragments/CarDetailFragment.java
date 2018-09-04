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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.adapter.ListAdapter;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Model;
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

public class CarDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.carDetailImageView)
    ImageView carDetailImageView;

    @BindView(R.id.detailsTextView)
    TextView detailsTextView;

    @BindView(R.id.buyCarDetail)
    Button carDetailButton;

    private int id;
    private Car car;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_detail, container, false);
        ButterKnife.bind(this,view);

        carDetailButton.setOnClickListener(this);
        try {
            car = (Car) ObjectSerializer.deserialize(SaveSharedPreference.getUsedCar(getContext()));
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

        return view;
    }


    @Override
    public void onClick(View view) {
        Log.i("Clicked", "You clicked this button!");

    }
}
