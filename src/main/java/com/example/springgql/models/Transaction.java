package com.example.springgql.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    String title;
    Category category;
    double amount;
    Date date;
}
