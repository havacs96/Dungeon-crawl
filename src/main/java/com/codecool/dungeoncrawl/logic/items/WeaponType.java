package com.codecool.dungeoncrawl.logic.items;

public enum WeaponType {

    CROSSBOW("crossbow", 50, 100),
    SWORD("sword", 75, 75),
    AXE("axe", 100, 50);

    private final String weaponName;
    private int plusHealthPoints;
    private int plusStrength;


    WeaponType(String weaponName, int plusHealthPoints, int plusStrength) {
        this.weaponName = weaponName;
        this.plusHealthPoints = plusHealthPoints;
        this.plusStrength = plusStrength;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getPlusHealthPoints() {
        return plusHealthPoints;
    }

    public int getPlusStrength() {
        return plusStrength;
    }
}
