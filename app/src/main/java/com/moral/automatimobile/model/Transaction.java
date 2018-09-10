package com.moral.automatimobile.model;

import com.google.gson.annotations.SerializedName;

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
public class Transaction {

    @SerializedName("id")
    private int id;

    @SerializedName("amount")
    private double amount;

    @SerializedName("transactionDate")
    private String transactionDate;

    @SerializedName("description")
    private String description;

    @SerializedName("person")
    private Person person;

    @SerializedName("creditCardNumber")
    private String creditCardNumber;

    public Transaction(double amount, String description, Person person, String creditCardNumber) {
        this.amount = amount;
        this.description = description;
        this.person = person;
        this.creditCardNumber = creditCardNumber;
    }
}
