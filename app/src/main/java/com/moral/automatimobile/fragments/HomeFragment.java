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
import android.widget.Button;

import com.moral.automatimobile.R;
import com.moral.automatimobile.session.SaveSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.newCarsButton)
    Button newCarsButton;

    @BindView(R.id.usedCarsButton)
    Button usedCarsBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, view);

        newCarsButton.setOnClickListener(this);
        usedCarsBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onModel(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();

        Log.i("Button-Text", buttonText);

        loadFragment(buttonText);

//        Intent intent = new Intent(getApplicationContext(), ModelActivity.class);
//        if(buttonText.equals("New Cars")) {
//            intent.putExtra("Car", "newCar");
//        } else {
//            intent.putExtra("Car", "usedCar");
//        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

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
        Button button = (Button)view;
        String buttonText = button.getText().toString();

        Log.i("Button-Text", buttonText);

        loadFragment(buttonText);
    }
}
