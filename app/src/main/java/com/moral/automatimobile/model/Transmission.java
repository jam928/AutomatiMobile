
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

public class Transmission {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("imgSrc")
    private String imgSrc;

    @SerializedName("imgAlt")
    private String imgAlt;

    @SerializedName("description")
    private String description;

    @SerializedName("transmissionPrice")
    private int transmissionPrice;

}
