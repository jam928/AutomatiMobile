package com.moral.automatimobile.services;

import com.moral.automatimobile.model.Car;
import com.moral.automatimobile.model.Color;
import com.moral.automatimobile.model.Engine;
import com.moral.automatimobile.model.Model;
import com.moral.automatimobile.model.Transmission;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarService {

    @GET("models")
    Call<List<Model>> getModels();

    @GET("cars/model")
    Call<List<Car>> getCarsByModel(@Query("model") String modelName);

    @GET("car")
    Call<Car> getCarById(@Query("id") int id);

    @GET("colors")
    Call<List<Color>> getColors();

    @GET("transmissions")
    Call<List<Transmission>> getTransmissions();

    @GET("engines")
    Call<List<Engine>> getEngines();

}
