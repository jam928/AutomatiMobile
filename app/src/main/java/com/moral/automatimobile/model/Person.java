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

public class Person {

    @SerializedName("id")
    private int id;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("street")
    private String street;

    @SerializedName("city")
    private String city;

    @SerializedName("password")
    private String password;

    @SerializedName("state")
    private State state;

    @SerializedName("role")
    private Role role;

    @SerializedName("balance")
    private int balance;

}
