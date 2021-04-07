package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Key extends Item {

    public final String KeyType;

    public Key(Cell cell, KeyType type) {
        super(cell);
        KeyType = type.getKeyName();
    }

    @Override
    public String getTileName() {
        return KeyType;
    }


    @Override
    public void setType(String type) {
        super.setType("Key");
    }
}
