package com.moral.automatimobile.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor

public class Jwt {

    @SerializedName("jwt")
    private String jwt;

    @SerializedName("isJWT")
    private Boolean isJWT;

}
