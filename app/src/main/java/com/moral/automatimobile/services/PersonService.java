package com.moral.automatimobile.services;

import com.moral.automatimobile.model.Jwt;
import com.moral.automatimobile.model.Person;
import com.moral.automatimobile.model.Shipping;
import com.moral.automatimobile.model.State;
import com.moral.automatimobile.model.StatusCheck;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PersonService {

    @GET("user/state")
    Call<List<State>> getStates();

    @POST("user/login")
    @Headers({"Content-Type: application/json"})
    Call<Jwt> login(@Body Person person);

    @POST("user/register")
    @Headers({"Content-Type: application/json"})
    Call<StatusCheck> register(@Body Person person);

    @GET("user/userEmail")
    Call<Person> getPersonByEmail(@Query("email") String email);

    @POST("user/updateShipping")
    @Headers({"Content-Type: application/json"})
    Call<StatusCheck> saveShipping(@Body Shipping shipping);
}
