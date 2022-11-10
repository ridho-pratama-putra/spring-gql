package com.example.springgql.enums;

/*DONT CHANGE THE ORDER*/
public enum CategoryEnum {

    ROCK("Rock"),
    POP("Pop");

    String displayText;

    CategoryEnum(String displayText) {
        this.displayText = displayText;
    }
}
