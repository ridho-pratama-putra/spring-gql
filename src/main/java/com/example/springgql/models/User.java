package com.example.springgql.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    Long id;
    String name;
    List<Transaction> transactions;
}
