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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.faqButton)
    Button faqButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        ButterKnife.bind(this, view);

        faqButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        Log.i("Clicked", buttonText);

        loadFragment(buttonText);
    }

    private void loadFragment(String buttonText) {
        Fragment fragment;
        if(buttonText.equals("FAQ'S")) {
            fragment = new FaqFragment();
        }else {
            fragment = null;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
