package com.moral.automatimobile.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CreditCard implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("number")
    private String creditCardNumber;

    @SerializedName("expDate")
    private String expirationDate;

    @SerializedName("csc")
    private int csc;

    @SerializedName("person")
    private Person person;

    public CreditCard(String creditCardNumber, String expirationDate, int csc, Person person) {
        this.creditCardNumber = creditCardNumber;
        this.expirationDate = expirationDate;
        this.csc = csc;
        this.person = person;
    }
}
