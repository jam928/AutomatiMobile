
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
public class Condition implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private String type;

    @SerializedName("description")
    private String description;

    public Condition(String type) {
        this.type = type;
    }
}
