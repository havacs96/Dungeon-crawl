package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Potion extends Item {

    private final String potionName;
    private final int healthPlus;
    private final PotionType type;

    public Potion(Cell cell, PotionType type) {
        super(cell);
        this.type = type;
        this.potionName = type.getPotionName();
        this.healthPlus = type.getHealthPlus();
    }

    @Override
    public String getTileName() {
        return potionName;
    }

}
