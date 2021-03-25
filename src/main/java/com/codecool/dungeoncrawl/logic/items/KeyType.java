package com.codecool.dungeoncrawl.logic.items;

public enum KeyType {

    BRONZE_KEY("bronze"),
    SILVER_KEY("silver");

    private final String keyName;

    KeyType(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

}
