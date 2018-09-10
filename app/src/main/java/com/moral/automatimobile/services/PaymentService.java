package com.moral.automatimobile.services;

import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.StatusCheck;
import com.moral.automatimobile.model.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentService {

    @POST("creditCard")
    @Headers({"Content-Type: application/json"})
    Call<StatusCheck> saveCreditCard(@Body CreditCard card);

    @POST("transaction")
    @Headers({"Content-Type: application/json"})
    Call<StatusCheck> addTransaction(@Body Transaction transaction);

    @GET("creditCard")
    Call<List<CreditCard>> getCreditCardsByEmail(@Query("email") String email);
}
