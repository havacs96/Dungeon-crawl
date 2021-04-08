package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Weapon extends Item {

    public final String weaponName;

    public Weapon(Cell cell, WeaponType weaponName) {
        super(cell);
        this.weaponName = weaponName.getWeaponName();
        itemType = "Weapon";
    }

    @Override
    public String getTileName() {
        return weaponName;
    }
}
