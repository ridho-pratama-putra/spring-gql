package com.example.springgql.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    String title;
    String description;
    double amount;
}
