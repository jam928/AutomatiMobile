package com.moral.automatimobile.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Role;
import com.moral.automatimobile.model.State;
import com.moral.automatimobile.model.StatusCheck;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.services.PersonService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.firstNameEditText)
    EditText firstNameEditText;

    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;

    @BindView(R.id.emailEditText)
    EditText emailEditText;

    @BindView(R.id.addressEditText)
    EditText addressEditText;

    @BindView(R.id.cityEditText)
    EditText cityEditText;

    @BindView(R.id.spinnerState)
    Spinner statesSpinner;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @BindView(R.id.registerButton)
    Button registerButton;

    String stateSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initiate the private fields
        ButterKnife.bind(this);

        // Get states from the restend point

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

        // Set onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }


    private void loadStateList(List<State> states) {
        final List<String> stateName = new ArrayList<>();
        for(State state: states) {
            stateName.add(state.getName());
        }

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stateName);

        statesSpinner.setAdapter(arrayAdapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateSelected = stateName.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void register() {

        final String firstName = firstNameEditText.getText().toString();
        final String lastName = lastNameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        final String address = addressEditText.getText().toString();
        final String city = cityEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if(!validate(firstName, lastName, email, address, city, password)){
            registerFailed();
            return;
        }

        // Set the spinning wheel thingy
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // Add 3 second delay to connect to the spring server and spit back a result
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                addPerson(firstName, lastName, email, address, city, password);
                progressDialog.dismiss();
            }
        }, 3000);



//        Toast.makeText(getApplicationContext(), "You made it pass validation!", Toast.LENGTH_LONG).show();



    }

    private void addPerson(String firstName, String lastName, String email, String address, String city, String password) {
        Role role = new Role();
        role.setName("customer");

        State state = new State(stateSelected);

        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setStreet(address);
        person.setCity(city);
        person.setPassword(password);
        person.setRole(role);
        person.setState(state);
        person.setBalance(0);

        Log.i("Person", person.toString());

        Call<StatusCheck> call = RetrofitClient.getInstance().getPersonService().register(person);
        call.enqueue(new Callback<StatusCheck>() {
            @Override
            public void onResponse(Call<StatusCheck> call, Response<StatusCheck> response) {
                if(response.isSuccessful()) {
                    Log.i("Response", response.body().toString());
                    registerFinish();
                } else {
                    Log.i("Response Error", response.errorBody().toString());
                    registerFailed();
                }
            }

            @Override
            public void onFailure(Call<StatusCheck> call, Throwable t) {

            }
        });
    }

    private boolean validate(String firstName, String lastName, String email, String address, String city, String password) {
        boolean isValid = true;

        if(firstName.isEmpty()) {
            firstNameEditText.setError("Enter a first name!");
            isValid = false;
        } else {
            firstNameEditText.setError(null);
        }
        if(lastName.isEmpty()) {
            lastNameEditText.setError("Enter a last name!");
            isValid = false;
        } else {
            lastNameEditText.setError(null);
        }
        // Check if the email has the correct email pattern
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a valid email address");
            isValid = false;
        } else{
            emailEditText.setError(null);
        }
        if(address.isEmpty()) {
            addressEditText.setError("Enter a address!");
            isValid = false;
        } else {
            emailEditText.setError(null);
        }

        if(city.isEmpty()) {
            cityEditText.setError("Enter a city!");
            isValid = false;
        } else {
            cityEditText.setError(null);
        }

        String pattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}";
        // Check if the password has the 4 to 12 characters and the correct pattern
        if(password.isEmpty() || !password.matches(pattern)) {
            passwordEditText.setError("Must enter a password that is at least 8 characters long and One captial and lower case character");
            isValid = false;
        } else {
            passwordEditText.setError(null);
        }

        return isValid;
    }
    private void registerFinish() {
        Toast.makeText(getApplicationContext(), "User successfully added!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void registerFailed() {
        Toast.makeText(this, "Register failed", Toast.LENGTH_LONG).show();
    }
}
