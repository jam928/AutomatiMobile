package com.moral.automatimobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.moral.automatimobile.R;
import com.moral.automatimobile.fragments.HomeFragment;
import com.moral.automatimobile.fragments.LoginFragment;
import com.moral.automatimobile.fragments.ProfileFragment;
import com.moral.automatimobile.fragments.RegisterFragment;
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

        // load the default fragment
        loadFragment(new HomeFragment());

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
            Intent intent;
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    return loadFragment(fragment);
                case R.id.navigation_login:
                    fragment = new LoginFragment();
                    return loadFragment(fragment);
                case R.id.navigation_register:
                    fragment = new RegisterFragment();
                    return loadFragment(fragment);
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    return loadFragment(fragment);
                case R.id.navigation_logout:
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false, "none");
                    finish();
                    startActivity(getIntent());
                    return true;
                }
                return false;
            }
    };



    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

//    public void onClick(View view) {
//        Button button = (Button)view;
//        String buttonText = button.getText().toString();
//
//        Log.i("Button-Text", buttonText);
//
//        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
//        if(buttonText.equals("New Cars")) {
//            intent.putExtra("Car", "newCar");
//        } else {
//            intent.putExtra("Car", "usedCar");
//        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//
//    }



}
