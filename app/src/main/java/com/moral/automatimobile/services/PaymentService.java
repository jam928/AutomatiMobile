package com.moral.automatimobile.services;

import com.moral.automatimobile.model.CreditCard;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.StatusCheck;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PaymentService {

    @POST("creditCard")
    @Headers({"Content-Type: application/json"})
    Call<StatusCheck> saveCreditCard(@Body CreditCard card);

}
