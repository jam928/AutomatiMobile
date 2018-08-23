
package com.moral.automatimobile.model;


import com.google.gson.annotations.SerializedName;

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
public class Engine {

    @SerializedName("id")
    private int id;

    @SerializedName("cylinders")
    private int cylinders;

    @SerializedName("litres")
    private double litres;

    @SerializedName("stockEnginePrice")
    private int stockEnginePrice;


}
