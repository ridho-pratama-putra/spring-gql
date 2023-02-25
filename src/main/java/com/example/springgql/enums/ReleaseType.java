package com.example.springgql.enums;

public enum ReleaseType {
    ALBUM("Album"),
    SINGLE("Single");

    String displayText;

    ReleaseType(String displayText) {
        this.displayText = displayText;
    }
}
