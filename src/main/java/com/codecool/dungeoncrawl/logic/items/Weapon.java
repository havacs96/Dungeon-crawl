package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Weapon extends Item {

    public final String weaponName;

    public Weapon(Cell cell, String weaponName) {
        super(cell);
        this.weaponName = weaponName;
    }

    @Override
    public String getTileName() {
        return weaponName;
    }

}
