package com.moral.automatimobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.session.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        boolean isLoggedIn = SaveSharedPreference.getLoggedStatus(getApplicationContext());

        if(isLoggedIn) {
            navigation.getMenu().removeItem(R.id.navigation_login);
            navigation.getMenu().removeItem(R.id.navigation_register);
        } else {
            navigation.getMenu().removeItem(R.id.navigation_profile);
            navigation.getMenu().removeItem(R.id.navigation_logout);
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_login:
                    // Change Intent
                    goToPage("Login");
                    return true;
                case R.id.navigation_register:
                    goToPage("Register");
                    return true;
                case R.id.navigation_profile:
                    goToPage("Profile");
                    return true;
                case R.id.navigation_logout:
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                    finish();
                    startActivity(getIntent());
                    return true;
                }
                return false;
            }
    };

    private void goToPage(String page) {
        Intent intent;
        switch (page) {
            case "Login":
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case "Register":
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case "New":
                intent = new Intent(getApplicationContext(), ModelActivity.class);
                intent.putExtra("New","newCar");
                startActivity(intent);
                break;
            case "Used":
                intent = new Intent(getApplicationContext(), ModelActivity.class);
                intent.putExtra("Used","usedCar");
                startActivity(intent);
                break;
        }
    }

    public void onClick(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();

        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
        if(buttonText.equals("New Cars")) {
            intent.putExtra("Car", "newCar");
        } else {
            intent.putExtra("Car", "usedCar");
        }
        startActivity(intent);
    }



}