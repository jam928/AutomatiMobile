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
import android.widget.ListView;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.adapter.ListAdapter;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.network.RetrofitClient;
import com.moral.automatimobile.session.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelsFragment extends Fragment {

    @BindView(R.id.modelsListView)
    ListView modelsListView;

    @BindView(R.id.loginCondTextView)
    TextView loginCondTextView;

    private ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_model, container, false);
        ButterKnife.bind(this,view);

        // check if the user is logged in
        boolean isLoggedIn = SaveSharedPreference.getLoggedStatus(getContext());

        if(isLoggedIn) {
            loginCondTextView.setText("Select Model...");
        } else {
            loginCondTextView.setVisibility(View.VISIBLE);
        }


        // Get models from the rest end point
        Call<List<Model>> call = RetrofitClient.getInstance().getCarService().getModels();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if(response.isSuccessful()) {
                    Log.i("Response", "Success");
                    List<Model> models = response.body();
                    loadModelsToView(models);
                } else {
                    Log.i("Response", "Failed");
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.i("Response", "Failed");
            }
        });
        return view;
    }

    private void loadModelsToView(final List<Model> models) {
        final List<String> imgSrcs = new ArrayList<>();
        final List<String> topInfo = new ArrayList<>();
        List<String> bottomInfo = new ArrayList<>();

        for(Model model: models) {
            imgSrcs.add(model.getImgSrc());
            topInfo.add("Automati " + model.getName());
            bottomInfo.add(model.getDescription());
        }

        this.listAdapter = new ListAdapter(getContext(), topInfo, bottomInfo, imgSrcs);
        modelsListView.setAdapter(this.listAdapter);

        modelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SaveSharedPreference.setModel(getContext(), models.get(i));
                Fragment fragment = null;
                if(SaveSharedPreference.getCarCondition(getContext()).equals("New Cars")) {
                    fragment = new NewFragment();
                } else {
                    fragment = new UsedFragment();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                }
        });
    }
}
