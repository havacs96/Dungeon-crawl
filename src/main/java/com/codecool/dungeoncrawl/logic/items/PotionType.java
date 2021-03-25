package com.codecool.dungeoncrawl.logic.items;

public enum PotionType {

    STRONG_HEALTH_POTION("strong healing potion", 40),
    WEAK_HEALTH_POTION("weak healing potion", 20),
    EXTRA_HEALTH_POTION("extra healing potion", 100);

    private final String potionName;
    private final int healthPlus;

    PotionType(String potionName, int healthPlus) {
        this.potionName = potionName;
        this.healthPlus = healthPlus;
    }

    public String getPotionName() {
        return potionName;
    }

    public int getHealthPlus() {
        return healthPlus;
    }

}
