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
import android.widget.ListView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.adapter.ListAdapter;
import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.serializer.ObjectSerializer;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsedFragment extends Fragment{

    @BindView(R.id.usedModelsListView)
    ListView usedModelsListView;

    private Model model;
    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_used,container,false);
        ButterKnife.bind(this, view);


        try {
            model = (Model) ObjectSerializer.deserialize(SaveSharedPreference.getModel(getContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Model", model.toString());


        Call<List<Car>> call = RetrofitClient.getInstance().getCarService().getCarsByModel(model.getName());
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if(response.isSuccessful()) {
                    Log.i("Response", "Success");

                    List<Car> cars = response.body();

                    loadCarsToView(cars);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Log.i("Response", "Fail");
            }
        });

        return view;
    }

    private void loadCarsToView(final List<Car> cars) {
        final List<String> imgSrcs = new ArrayList<>();
        final List<String> topInfo = new ArrayList<>();
        List<String> bottomInfo = new ArrayList<>();

        for(Car car: cars) {
            imgSrcs.add(car.getModel().getImgSrc());
            bottomInfo.add("$" + car.getPrice() + " Mileage: " + car.getMileage());
            topInfo.add(car.getYear() + " Automati " + car.getModel().getName());
        }

        this.listAdapter = new ListAdapter(getContext(), topInfo, bottomInfo, imgSrcs);
        usedModelsListView.setAdapter(this.listAdapter);

        usedModelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("TopInfo", topInfo.get(i));
                Fragment fragment = new CarDetailFragment();
                SaveSharedPreference.setUsedCar(getContext(), cars.get(i));
                loadFragment(fragment);
//                Intent intent = new Intent(getApplicationContext(), CarDetailActivity.class);

            }
        });
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

}