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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.State;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.nameShippingEditText)
    EditText nameShippingEditText;

    @BindView(R.id.addressShippingEditText)
    EditText addressShipEditText;

    @BindView(R.id.cityShippingEditText)
    EditText cityShipEditText;

    @BindView(R.id.stateShippingSpinner)
    Spinner stateShippingSpinner;

    @BindView(R.id.shippingButton)
    Button shippingButton;

    @BindView(R.id.savedAddressRadioGroup)
    RadioGroup savedAddressRG;
    String stateSelected = "";

    private Person person;
    private List<Shipping> savedAddresses;
    private boolean radioButtonClicked;
    private int addressSelectedIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping,container,false);
        ButterKnife.bind(this, view);

        shippingButton.setOnClickListener(this);

        Call<List<State>> call = RetrofitClient.getInstance().getPersonService().getStates();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                loadStateList(response.body());
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load states", Toast.LENGTH_LONG).show();
            }
        });


        try {
            person = (Person) ObjectSerializer.deserialize(SaveSharedPreference.getPerson(getContext()));
        }catch (Exception e) {
            e.printStackTrace();
        }

        Call<List<Shipping>> call2 = RetrofitClient.getInstance().getPersonService().getShippingAddressByEmail(SaveSharedPreference.getEmail(getContext()));
        call2.enqueue(new Callback<List<Shipping>>() {
            @Override
            public void onResponse(Call<List<Shipping>> call, Response<List<Shipping>> response) {
                if(response.isSuccessful()) {
                    savedAddresses = response.body();


                    int buttons = savedAddresses.size();
                    for(int i = 0; i < buttons; i++) {
                        RadioButton radioButton = new RadioButton(getContext());
                        radioButton.setId(i);
                        String radioBtnText = "Name: " + savedAddresses.get(i).getFirstName() + " " + savedAddresses.get(i).getLastName() + "\n" +
                                "Address: " + savedAddresses.get(i).getStreet() + "\n" +
                                "City: " + savedAddresses.get(i).getCity() + "\n"  +
                                "State: " + savedAddresses.get(i).getState().getName() + "\n";
                        radioButton.setText(radioBtnText);
                        savedAddressRG.addView(radioButton);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Shipping>> call, Throwable t) {

            }
        });

        savedAddressRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioButtonClicked = true;
                addressSelectedIndex = i;
            }
        });

        return view;
    }

    private void loadStateList(List<State> states) {
        final List<String> stateName = new ArrayList<>();
        for(State state: states) {
            stateName.add(state.getName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, stateName);

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

    @Override
    public void onClick(View view) {

        if(radioButtonClicked) {
            SaveSharedPreference.setShipping(getContext(),savedAddresses.get(addressSelectedIndex));
        } else {
            String name = nameShippingEditText.getText().toString();
            String address = addressShipEditText.getText().toString();
            String city = cityShipEditText.getText().toString();

            Log.i("Name", name);
            Log.i("Address", address);
            Log.i("City", city);
            Log.i("State Selected", stateSelected);

            if(!validShipping(name, address, city)) {
                Toast.makeText(getContext(), "Invalid shipping input", Toast.LENGTH_LONG).show();
                return;
            }
            String[] temp = name.split("\\s+");
            String firstName = temp[0];
            String lastName = temp[1];
            String email = SaveSharedPreference.getEmail(getContext());
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
            SaveSharedPreference.setShipping(getContext(),shipping);
        }
        Fragment fragment = new BuyFragment();
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