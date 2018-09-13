package com.moral.automatimobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moral.automatimobile.R;
import com.moral.automatimobile.model.Faq;
import com.moral.automatimobile.network.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqFragment extends Fragment {


    @BindView(R.id.faqTextView)
    TextView faqTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_faq,container,false);
        ButterKnife.bind(this, view);

        Call<List<Faq>> call = RetrofitClient.getInstance().getFaqService().getFaqs();
        call.enqueue(new Callback<List<Faq>>() {
            @Override
            public void onResponse(Call<List<Faq>> call, Response<List<Faq>> response) {
                if(response.isSuccessful()) {
                    List<Faq> faqs = response.body();
                    String faqInfo = "";
                    for(Faq faq: faqs) {
                        faqInfo += "Q: " + faq.getQuestion() + "\n" +
                                "A: " + faq.getAnswer() + "\n\n";
                    }
                    faqTextView.setText(faqInfo);
                }
            }

            @Override
            public void onFailure(Call<List<Faq>> call, Throwable t) {
                Log.i("Error", "Fetching the faqs");
            }
        });

        return view;
    }




}
