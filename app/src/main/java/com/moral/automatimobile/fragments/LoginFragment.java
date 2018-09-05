package com.moral.automatimobile.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moral.automatimobile.R;
import com.moral.automatimobile.activity.LoginActivity;
import com.moral.automatimobile.activity.MainActivity;
import com.moral.automatimobile.model.Jwt;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.session.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.emailEditTxt)
    EditText emailTxt;

    @BindView(R.id.passwordEditTxt)
    EditText passwordTxt;

    @BindView(R.id.loginButton)
    Button loginBtn;

    @BindView(R.id.forgotPasswordButton)
    Button forgotPasswordButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this, view);

        loginBtn.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        // if the onclick is a button
        if(buttonText.equals("Login")) {
            final String email = emailTxt.getText().toString();
            final String password = passwordTxt.getText().toString();

            // Check if the credentials are valid input
            if(!validatation(email, password)) {
                loginFailed();
                return;
            }

            Log.i("Valid", "Yes");

            // Set the spinning wheel thingy
            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            // Add 3 second delay to connect to the spring server and spit back a result

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            checkCredentials(email, password);

                            // Close the spiny thingy
                            progressDialog.dismiss();
                        }
                    }, 3000);
        } else {
            Log.i("Clicked", "Signup Button");
        }
    }

    // Validate the email and text field
    private boolean validatation(String email, String password) {
        boolean result = true;

        // Check if the email has the correct email pattern
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailTxt.setError("Enter a valid email address");
            result = false;
        } else{
            emailTxt.setError(null);
        }

        String pattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}";
        // Check if the password has the 4 to 12 characters and the correct pattern
        if(password.isEmpty() || !password.matches(pattern)) {
            passwordTxt.setError("Must enter a password that is at least 8 characters long and One captial and lower case character");
            result = false;
        } else {
            passwordTxt.setError(null);
        }

        return result;
    }

    private void loginFailed() {
        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
    }

    private void checkCredentials(final String email, String password) {

        Person person = new Person();
        person.setEmail(email);
        person.setPassword(password);
        Call<Jwt> call = RetrofitClient.getInstance().getPersonService().login(person);
        call.enqueue(new Callback<Jwt>() {
            @Override
            public void onResponse(Call<Jwt> call, @NonNull Response<Jwt> response) {
                Log.i("CODE", Integer.toString(response.code()));
                if (response.isSuccessful()) {
                    if(response.body().getIsJWT()) {
                        Toast.makeText(getContext(), "You Successfully Logged In", Toast.LENGTH_LONG).show();
                        SaveSharedPreference.setLoggedIn(getContext(), true, email);
                        loginSuccess();
                    }
                    else {
                        loginFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<Jwt> call, Throwable t) {
                Toast.makeText(getContext(), "Network Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginSuccess() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
