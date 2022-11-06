package com.example.springgql.enums;

/*DONT CHANGE THE ORDER*/
public enum Category {

    ROCK("Rock"),
    POP("Pop");

    String displayText;

    Category(String displayText) {
        this.displayText = displayText;
    }
}
