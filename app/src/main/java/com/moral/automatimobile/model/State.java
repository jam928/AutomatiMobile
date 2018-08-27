
package com.moral.automatimobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Getter
@Setter
public class State implements Serializable{

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

}