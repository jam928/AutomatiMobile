
package com.moral.automatimobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Car implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("year")
    private int year;

    @SerializedName("mileage")
    private int mileage;

    @SerializedName("title")
    private String title;

    @SerializedName("model")
    private Model model;

    @SerializedName("color")
    private Color color;

    @SerializedName("transmission")
    private Transmission transmission;

    @SerializedName("engine")
    private Engine engine;

    @SerializedName("condition")
    private Condition condition;

    @SerializedName("epa")
    private Object epa;

    @SerializedName("price")
    private int price;

    @SerializedName("lease")
    private Object lease;

    @SerializedName("vin")
    private String vin;

    @SerializedName("reviewed")
    private boolean reviewed;

    @SerializedName("person")
    private Person person;


}
