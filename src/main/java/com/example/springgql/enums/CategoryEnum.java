package com.example.springgql.enums;

/*DONT CHANGE THE ORDER*/
public enum CategoryEnum {

    ROCK("Rock"),
    POP("Pop"),
    JAZZ("Jazz"),
    RNB("RNB"),
    PUNK("Punk"),
    COUNTRY("Country"),
    METAL("Metal"),
    ;

    String displayText;

    CategoryEnum(String displayText) {
        this.displayText = displayText;
    }
}
