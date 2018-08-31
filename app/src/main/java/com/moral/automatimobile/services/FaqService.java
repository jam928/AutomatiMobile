package com.moral.automatimobile.services;

import com.moral.automatimobile.model.Faq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FaqService {

    @GET("getFaq")
    Call<List<Faq>> getFaqs();
}
