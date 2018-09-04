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

public class NewFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.newCarImageView)
    ImageView newCarImageView;

    @BindView(R.id.colorSpinner)
    Spinner colorSpinner;

    @BindView(R.id.transmissionSpinner)
    Spinner transmissionSpinner;

    @BindView(R.id.engineSpinner)
    Spinner engineSpinner;

    @BindView(R.id.onToPayButton)
    Button onToPayButton;

    private List<Color> colors;
    private List<Transmission> transmissions;
    private List<Engine> engines;
    private int colorSelected;
    private int transmissionSelected;
    private int engineSelected;

    private Model model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new,container,false);
        ButterKnife.bind(this, view);

        onToPayButton.setOnClickListener(this);
        try {
            model = (Model) ObjectSerializer.deserialize(SaveSharedPreference.getModel(getContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get car properties
        loadCarProperties();

        return view;
    }

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
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, colorInfo);

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
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, transmissionInfo);

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
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, engineInfo);

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

        SaveSharedPreference.setNewCarProperties(getContext(), colors.get(colorSelected), transmissions.get(transmissionSelected), engines.get(engineSelected));
        SaveSharedPreference.setCarCondition(getContext(), "New Car");

//        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
////        startActivity(intent);
////        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    private void loadFragment(String buttonText) {
        SaveSharedPreference.setCarCondition(getContext(), buttonText);
        Fragment fragment = new ModelsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        Log.i("Clicked", "You clicked this button!");
    }
}